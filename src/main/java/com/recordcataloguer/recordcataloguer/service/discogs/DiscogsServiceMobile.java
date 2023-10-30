package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsUserCredentials;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.constants.DiscogsConstants;
import com.recordcataloguer.recordcataloguer.helpers.hibernate.HibernateUtil;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.PriceSuggestionResponse;
import com.recordcataloguer.recordcataloguer.helpers.discogs.auth.DiscogsAuthHelper;
import com.recordcataloguer.recordcataloguer.helpers.discogs.validators.DiscogsSearchResultValidator;
import com.recordcataloguer.recordcataloguer.helpers.string.StringHelper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DiscogsServiceMobile {


    /****TODO:
     *Add way to determine if album is actual duplicate
     *      a. apache getCommonPrefix????
     * ***/

    @Autowired
    private DiscogsClient discogsClient;

    @Autowired
    private DiscogsService discogsService;

    /***
     * Get all records by catalog number only, without analyzing image.
     * @param catalogNumber
     * @return List<Album> filteredAlbums with metadata
     */
    public List<Album> getAlbumsByCatalogNumberFromMobile(String catalogNumber) {
        log.info("received request to getAlbumsByCatalogNumberFromMobile with catNo {}", catalogNumber);

        DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogNumber, DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, DiscogsConstants.COUNTRY, DiscogsConstants.VINYL_FORMAT, "");

        if(discogsSearchResponse.getAlbums().isEmpty()) return new ArrayList<>();

        List<Album> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(discogsSearchResponse.getAlbums());
        getPriceSuggestions(filteredAlbums);

        return filteredAlbums;
    }


    /***
     * Get all records by catalog number only, without analyzing image and persist to DB
     * @param catalogNumber
     * @return
     */
    public List<Album> getRecordsByCatalogNumber(String catalogNumber) {
        log.info("received request to getRecordsByCatalogNumber with catalogNumber {}", catalogNumber);

        DiscogsSearchResponse albums = discogsClient.getDiscogsRecordByCategoryNumber(
                catalogNumber, DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, DiscogsConstants.COUNTRY, DiscogsConstants.VINYL_FORMAT, "");

        List<Album> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(albums.getAlbums());
        List<Album> albumsWithPricing = getPriceSuggestions(filteredAlbums);

        try {
            log.info("Persisting {} albums to DB", filteredAlbums.size());
            HibernateUtil.persistAlbumsToDBController(albumsWithPricing);
        } catch (Exception e) {
            log.error("Error persisting albums to DB for catNo {} \n {}", e.getMessage());
        }

        return albumsWithPricing;
    }

    /***
     * Publish album to uncategorized collection.
     * @param releaseId
     * @return
     */
    public HttpStatus publishAlbumToUserCollection(String releaseId, int folderId) {
        log.info("received request to publishAlbum with releaseId {} and folderId {}", releaseId, folderId);
        // If folderId is not specified, default is 1 (Uncategorized)
        if(folderId == 0) folderId = 1;

        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        HttpStatus publishResponse = discogsClient.uploadAlbumToCollection(authHeader, DiscogsUserCredentials.DISCOGS_USERNAME, folderId, releaseId);

        return publishResponse;
    }

    /***
     * Get Price Suggestions from Discogs for a single given album.
     * @param releaseId
     * @return
     * @throws FeignException
     */
    public PriceSuggestionResponse getPriceSuggestions(String releaseId) throws FeignException {
        log.info("received request to getPriceSuggestions");

        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        PriceSuggestionResponse priceSuggestionsResponse = discogsClient.getPriceSuggestions(authHeader, releaseId);

        return priceSuggestionsResponse;
    }

    /***
     * Get Price Suggestions from Discogs for a given list of albums.
     * @param albums
     * @return List<Album> -- With price suggestions
     */
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
            catch (FeignException | NullPointerException feignException) {
                log.info("Exception assigning album value to release {} and catno {} with message\n {}", album.getReleaseId(), album.getCatno(), feignException.getMessage());
            }

            resultsWithPriceSuggestions.add(album);
        }

        return resultsWithPriceSuggestions;
    }

/***************TODO: DELETE OR IMPLEMENT UNUSED METHODS****************/

    public List<Album> getNextPageOfResults(DiscogsSearchResponse response) {
        List<Album> allFilteredAlbums = new ArrayList<>();
        int nextPageNumber = Integer.parseInt(StringHelper.getSubstringParam(response.getPagination().getUrls().get("next"), "&page=", "EnD"));
        DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, DiscogsConstants.VINYL_FORMAT, 100, nextPageNumber);

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

    public String getAuthorizationUrl() {
        log.info("received request to retrieve user authorization URL");

        Optional<String> url = DiscogsAuthHelper.getOAuthToken();
        return url.orElse("");
    }
}
