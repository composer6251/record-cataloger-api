package com.recordcataloguer.recordcataloguer.service.discogs;

import com.google.cloud.vision.v1.EntityAnnotation;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.helpers.hibernate.HibernateUtil;
import com.recordcataloguer.recordcataloguer.dto.discogs.request.DiscogsSearchAlbumRequest;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.helpers.discogs.DiscogsServiceHelper;
import com.recordcataloguer.recordcataloguer.helpers.discogs.validators.DiscogsSearchResultValidator;
import com.recordcataloguer.recordcataloguer.helpers.image.vision.ImageReader;
import com.recordcataloguer.recordcataloguer.helpers.string.StringHelper;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.PriceSuggestionResponse;
import com.recordcataloguer.recordcataloguer.helpers.discogs.auth.DiscogsAuthHelper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.recordcataloguer.recordcataloguer.helpers.regex.CatalogNumberRegex.CAT_NO_SIX_AND_GREATER;

@Service
@Slf4j
public class DiscogsService {

    @Autowired
    private DiscogsClient discogsClient;

    @Autowired
    private ImageReader imageReader;

    // TODO: Defaulting to "US" to minimize results/duplicates. User should have option to search everywhere on UI
    private String country = "US";
    private final String format = "vinyl";

    private final String token = DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN;

    public String extractTextFromImage(String url){

        return imageReader.extractRawVisionText(url);
    }

    public List<String> splitRawText(String text){

        return imageReader.splitRawVisionText(text);
    }

    public String filterImageTextForUser(String url){

        return imageReader.extractRawVisionText(url);
    }

    public List<String> getSearchStringsByImageVerticesUrl(String url, int separatorDistance){
        List<EntityAnnotation> annotations = imageReader.getVisionEntityAnnotations(url);
        List<String> results = DiscogsServiceHelper.getSearchStringsByImageVertices(annotations, separatorDistance);

        return results;

    }

    public List<Album> getRecordsByRegex(String imageUrl) {
        log.info("received request to getRecordsByRegex with imageUrl {}", imageUrl);

        List<Album> albums = getRecordsByImageUrl(imageUrl);

        List<Album> albumsWithPricing = getPriceSuggestions(albums);

        return albumsWithPricing;
    }

    public List<Album> getRecordsBySpineText(String imageUrl, int separatorDistance) {
        log.info("received request to getRecordsBySpineText with imageUrl {}", imageUrl);

        List<Album> discogsSearchResponse = getRecordsByAlbumSpineText(imageUrl, separatorDistance);

        return discogsSearchResponse;
    }

    public PriceSuggestionResponse getPriceSuggestions(String releaseId) throws FeignException {
        log.info("received request to getPriceSuggestions");

        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        PriceSuggestionResponse priceSuggestionsResponse = discogsClient.getPriceSuggestions(authHeader, releaseId);

        return priceSuggestionsResponse;
    }

    public String getAuthorizationUrl() {
        log.info("received request to retrieve user authorization URL");

        Optional<String> url = DiscogsAuthHelper.getOAuthToken();

        return url.orElse("");
    }

    /***
     * DiscogsService controller method for getting records by an image
     * @param imageUrl
     * @return ResponseEntity of DiscogsResponse
     */
    private List<Album> getRecordsByAlbumSpineText(String imageUrl, int separatorDistance) {

        /*****METHOD 1 Using AlbumNotation Objects, filtering based on initialXvert and YvertTotals*****/
        List<EntityAnnotation> annotations = imageReader.getVisionEntityAnnotations(imageUrl);
        List<String> individualAlbumSpineTexts = DiscogsServiceHelper.getSearchStringsByImageVertices(annotations, separatorDistance);
        List<Album> albumsToReturn = new ArrayList<>();
        for (String text : individualAlbumSpineTexts) {

            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByQueryString(text, token, country, format);

            if(discogsSearchResponse.getAlbums().isEmpty()) continue;

            // Sets property used to determine what used for lookup, NOT what was in the result
            for (Album album : discogsSearchResponse.getAlbums()) {
                album.setSearchQuery(text);
            }
            // TODO: May have to make sure records are separated by more distance

            List<Album> validatedAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(discogsSearchResponse.getAlbums());
            List<Album> validAlbums = DiscogsSearchResultValidator.filterOutByAnnotationDescriptionSimilar(discogsSearchResponse.getAlbums(), individualAlbumSpineTexts);
            List<Album> fullyValidated = DiscogsSearchResultValidator.filterOutByAnnotationDescriptionExact(discogsSearchResponse.getAlbums(), individualAlbumSpineTexts);
            albumsToReturn.addAll(validatedAlbums);
        }

        return albumsToReturn;

    }

    /****METHOD 2 using raw image texts and regexes******/
    public List<Album> getRecordsByImageUrl(String imageUrl) {
        String visionRawText = extractTextFromImage(imageUrl);

        if(visionRawText.isEmpty()) return null;
        // split extractedText into Individual strings
        List<DiscogsSearchAlbumRequest> searchAlbumRequestsByRegex = DiscogsServiceHelper.buildSearchRequestsFromRawText(visionRawText, CAT_NO_SIX_AND_GREATER);
        List<String> individualAlbumsTexts = DiscogsServiceHelper.getTextForIndividualAlbums(visionRawText);
        List<DiscogsSearchAlbumRequest> searchAlbumRequests = DiscogsServiceHelper.buildSearchRequestsFromIndividualStrings(individualAlbumsTexts, CAT_NO_SIX_AND_GREATER);
        // Create requests for catalogNumber

        return getRecordsBySearchRequest(searchAlbumRequests);
    }

    private List<Album> getRecordsBySearchRequest(List<DiscogsSearchAlbumRequest> albumRequests) {

        List<Album> albums = new ArrayList<>();
        int i = 41;
        for (DiscogsSearchAlbumRequest albumRequest : albumRequests) {
            i++;
            if(i % 20 == 0){
                log.info("Pausing SEARCH requests for 1 minute. requests size {}", albumRequests.size());
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String catNoOrTitle = "";

            //If no catNo but we have title, send title
            if(StringUtils.isBlank(albumRequest.getCatNo()) && !StringUtils.isBlank(albumRequest.getTitle())) {
                catNoOrTitle = albumRequest.getTitle();
            }
            else {
                catNoOrTitle = albumRequest.getCatNo();
            }
            // if request all props = then we should have the catNo
            // if request.catNo == null, is request.title alphanumeric? send request.title as catNo param Or add regex with multiple dashes?
            // todo: Throttle in a non-hacky way
            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catNoOrTitle, token, country, format, "");

            if(discogsSearchResponse.getAlbums().isEmpty()) continue;

            // Sets property used to determine what used for lookup, NOT what was in the result
            for (Album album : discogsSearchResponse.getAlbums()) {
                album.setSearchQuery(albumRequest.getCatNo());
            }

            List<Album> validatedAlbums = DiscogsSearchResultValidator.validateSearchResultsByRequestAndResults(discogsSearchResponse.getAlbums(), albumRequest, albumRequests);
            albums.addAll(validatedAlbums);
        }

        List<Album> validatedAlbums = albums.stream().filter(a -> a.getTitle() != null && (a.getReleaseId() != null || !a.getReleaseId().isEmpty())).collect(Collectors.toList());
        return validatedAlbums;
    }

    public List<Album> getPriceSuggestions(List<Album> albums) {
        List<Album> resultsWithPriceSuggestions = new ArrayList<>();
        log.info("Getting price suggestions for {} albums", albums.size());
        for (Album album : albums) {
            if(!Objects.nonNull(album)) continue;
            try{
                TimeUnit.SECONDS.sleep(1);

            }catch (Exception exception) {
                log.info("Exception sleeping thread {}", exception.getMessage());
            }
            try {
                PriceSuggestionResponse priceSuggestionResponse = getPriceSuggestions(album.getReleaseId());
                if(priceSuggestionResponse.getGood() != null) album.setAlbumGoodValue(priceSuggestionResponse.getGood().getValue());
                if(priceSuggestionResponse.getMint() != null) album.setAlbumMintPlusValue(priceSuggestionResponse.getMint().getValue());
            }
            catch (FeignException feignException) {
                log.info("Exception assiging album value to release {} and catno {} with message\n {}", album.getReleaseId(), album.getCatno(), feignException.getMessage());
            }
            catch (NullPointerException exception) {
                log.info("Exception assigning album value to release {} and catno {} with message\n {}", album.getReleaseId(), album.getCatno(), exception.getMessage());
            }

            resultsWithPriceSuggestions.add(album);
        }

        return resultsWithPriceSuggestions;
    }

    /***
     * Gets ALL albums from Discogs that contain a catalog number, and persists them to the DB
     * This was for research on catalog number format, and possibly for machine learning
     * to be able to better identify catalog number formats
     * @return List<AlbumEntity>
     */
    @SneakyThrows
    public List<AlbumEntity> getAllDiscogsCatalogNumbers() {

        DiscogsSearchResponse response = discogsClient.getAllDiscogsCatalogNumbers(token, format, 100);
        List<AlbumEntity> resultsToReturn = new ArrayList<>();
        AtomicReference<Long> failedTransactions = new AtomicReference<>(0L);

        int i = 1;
        while(response.getPagination() != null && response.getPagination().getUrls().containsKey("next") && i < 101) {
            if(i % 20 == 0){
                log.info("Pausing requests for 1 minute. result size {}", resultsToReturn.size());
                TimeUnit.MINUTES.sleep(1);
            }
            DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(token, format, 100, i++);

            HibernateUtil.persistAlbumsToDBController(resp.getAlbums());

            //resultsToReturn.addAll(allFilteredAlbums);
            log.info("resultsToReturn size {}", resultsToReturn.size());

        }
        log.info("Number of failed Record inserts {} and total results {}", failedTransactions, resultsToReturn);

        return resultsToReturn;
    }

    public List<Album> getNextPageOfResults(DiscogsSearchResponse response) {
        List<Album> allFilteredAlbums = new ArrayList<>();
            int nextPageNumber = Integer.parseInt(StringHelper.getSubstringParam(response.getPagination().getUrls().get("next"), "&page=", "EnD"));
            DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(token, format, 100, nextPageNumber);

            allFilteredAlbums = resp.getAlbums()
                    .stream()
                    .filter(r -> !Objects.equals(r.getCatno(), ""))
                    .map(result -> Album.builder().catno(result.getCatno()).country(result.getCountry()).build())
                    .collect(Collectors.toList());

        return allFilteredAlbums;
    }

    public String verifyIdentity() {
        log.info("received request to verify user identity");

        Optional<String> url = DiscogsAuthHelper.getOAuthToken();

        return url.orElse("");
    }
}
