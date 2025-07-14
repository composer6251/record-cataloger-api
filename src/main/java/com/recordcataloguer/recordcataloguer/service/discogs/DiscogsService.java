package com.recordcataloguer.recordcataloguer.service.discogs;

import com.google.cloud.vision.v1.EntityAnnotation;
import com.recordcataloguer.recordcataloguer.constants.DiscogsConstants;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsUserCredentials;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Listing;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.Release;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.UserCollectionByFolderResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.marketplaceapi.DiscogsUserInventoryResponse;
import com.recordcataloguer.recordcataloguer.database.hibernate.HibernateUtil;
import com.recordcataloguer.recordcataloguer.dto.discogs.request.DiscogsSearchAlbumRequest;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.util.discogs.DiscogsServiceHelper;
import com.recordcataloguer.recordcataloguer.util.discogs.validators.DiscogsSearchResultValidator;
import com.recordcataloguer.recordcataloguer.util.image.vision.ImageReader;
import com.recordcataloguer.recordcataloguer.util.string.StringHelper;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.PriceSuggestionResponse;
import com.recordcataloguer.recordcataloguer.util.discogs.auth.DiscogsAuthHelper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.recordcataloguer.recordcataloguer.util.regex.CatalogNumberRegex.CAT_NO_SIX_AND_GREATER;

@Service
@Slf4j
public class DiscogsService {

    @Autowired
    private DiscogsClient discogsClient;

//    @Autowired
//    private ImageReader imageReader;

    // TODO: Defaulting to "US" to minimize results/duplicates. User should have option to search everywhere on UI
    private String country = "US";
    private final String format = "vinyl";
    private final String token = DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN;


    /**************************USER COLLECTION METHODS*************************/
    public List<Release> getUserCollectionByFolderId(String userName, int folderId) {
        log.info("received request to getUserCollection with user name: {}", userName);

        String authorizationHeader = DiscogsAuthHelper.generateOAuthHeaderRequests(DiscogsTokens.DISCOG_OAUTH_TOKEN_FOR_USER_ACTION, DiscogsTokens.DISCOG_OAUTH_TOKEN_SECRET_FOR_USER_ACTION);
        UserCollectionByFolderResponse userCollectionResponse = discogsClient.getCollectionReleasesByFolderId(authorizationHeader, userName, folderId);
        // List<Listing> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(userCollectionResponse.getListings());

        return userCollectionResponse.getReleases();
    }

    /***
     * Publish album to uncategorized collection.
     * @param releaseId
     * @return HttpStatus
     */
    public HttpStatus publishAlbumToUserCollection(String releaseId, int folderId) {
        log.info("received request to publishAlbum with releaseId {} and folderId {}", releaseId, folderId);
        // If folderId is not specified, default is 1 (Uncategorized)
        if(folderId == 0) folderId = 1;

        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        HttpStatus publishResponse = discogsClient.uploadAlbumToCollection(authHeader, DiscogsUserCredentials.DISCOGS_USERNAME, folderId, releaseId);

        return publishResponse;
    }

    /**************************USER INVENTORY METHODS*************************/
    public List<Listing> getUserInventory(String userName) {
        log.info("received request to getUserCollection with user name: {}", userName);

        DiscogsUserInventoryResponse response = discogsClient.getUserInventoryByUserNameAndToken(DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, userName);
        String authorizationHeader = DiscogsAuthHelper.generateOAuthHeaderRequests(DiscogsTokens.DISCOG_OAUTH_TOKEN_FOR_USER_ACTION, DiscogsTokens.DISCOG_OAUTH_TOKEN_SECRET_FOR_USER_ACTION);
        DiscogsUserInventoryResponse userCollectionResponse = discogsClient.getUserInventoryByUserName(authorizationHeader, userName);
        // List<Listing> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(userCollectionResponse.getListings());

        return userCollectionResponse.getListings();
    }

    /**************************SEARCH DATABASE METHODS*************************/
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

    /***
     * HELPER METHOD
     * @param url
     * @param separatorDistance
     * @return
     */
    public List<String> getSearchStringsByImageVerticesUrl(String url, int separatorDistance){
//        List<EntityAnnotation> annotations = imageReader.getVisionEntityAnnotations(url);
//        List<String> results = DiscogsServiceHelper.getSearchStringsByImageVertices(annotations, separatorDistance);
//
//        return results;
        return new ArrayList<>();
    }

    /***
     * DiscogsService controller method for getting records by an image
     * @param imageUrl
     * @return ResponseEntity of DiscogsResponse
     */
    private List<Album> getRecordsByAlbumSpineText(String imageUrl, int separatorDistance) {

        /*****METHOD 1 Using AlbumNotation Objects, filtering based on initialXvert and YvertTotals*****/
       // List<EntityAnnotation> annotations = imageReader.getVisionEntityAnnotations(imageUrl);
        List<String> individualAlbumSpineTexts = DiscogsServiceHelper.getSearchStringsByImageVertices(new ArrayList<>(), separatorDistance);
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

    /***
     * Get all records by catalog number only, without analyzing image and persist to DB
     * @param catalogNumber
     * @return
     */
    public List<Album> getAlbumsByCatalogNumber(String catalogNumber) {
        log.info("received request to getRecordsByCatalogNumber with catalogNumber {}", catalogNumber);

        DiscogsSearchResponse albums = discogsClient.getDiscogsRecordByCategoryNumber(
                catalogNumber, DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, DiscogsConstants.COUNTRY, DiscogsConstants.VINYL_FORMAT, "");

        List<Album> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(albums.getAlbums());
        List<Album> albumsWithPricing = getPriceSuggestions(filteredAlbums);

        try {
            log.info("Persisting {} albums to DB", filteredAlbums.size());
            HibernateUtil.persistAlbumsToDBController(albumsWithPricing);
        } catch (Exception e) {
            log.error("Error persisting albums to DB for catalogNumber {} \n {}", catalogNumber, e.getMessage());
        }

        return albumsWithPricing;
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

    /**************************EXTRACT TEXT METHODS*************************/
    public String extractTextFromImage(String url){
       // return imageReader.extractRawVisionText(url);
        return null;
    }

//    public List<String> splitRawText(String text){
//        return imageReader.splitRawVisionText(text);
//    }

//    public String filterImageTextForUser(String url){
//        return imageReader.extractRawVisionText(url);
//    }
}
