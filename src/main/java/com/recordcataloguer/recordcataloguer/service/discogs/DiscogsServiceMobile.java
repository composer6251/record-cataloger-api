package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsUserCredentials;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.constants.DiscogsConstants;
import com.recordcataloguer.recordcataloguer.dto.discogs.request.OAuthRequest;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.*;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.DiscogsCollectionResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.Release;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.UserCollectionByFolderResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.marketplaceapi.DiscogsUserInventoryResponse;
import com.recordcataloguer.recordcataloguer.util.hibernate.HibernateUtil;
import com.recordcataloguer.recordcataloguer.util.discogs.auth.DiscogsAuthHelper;
import com.recordcataloguer.recordcataloguer.util.discogs.validators.DiscogsSearchResultValidator;
import com.recordcataloguer.recordcataloguer.util.string.StringHelper;
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


    /********USER COLLECTION METHODS******/

//    public List<Release> getUserCollectionByFolderId(String userName, int folderId) {
//        log.info("received request to getUserCollection with user name: {}", userName);
//
//        String authorizationHeader = DiscogsAuthHelper.generateOAuthHeaderForInventoryRequest(DiscogsTokens.DISCOG_OAUTH_TOKEN_FOR_USER_ACTION, DiscogsTokens.DISCOG_OAUTH_TOKEN_SECRET_FOR_USER_ACTION);
//        UserCollectionByFolderResponse userCollectionResponse = discogsClient.getCollectionReleasesByFolderId(authorizationHeader, userName, folderId);
//        // List<Listing> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(userCollectionResponse.getListings());
//
//        return userCollectionResponse.getReleases();
//    }


    /*********USER INVENTORY ENDPOINTS********/

//    public List<Listing> getUserInventory(String userName) {
//        log.info("received request to getUserCollection with user name: {}", userName);
//
//        DiscogsUserInventoryResponse response = discogsClient.getUserInventoryByUserNameAndToken(DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, userName);
//        String authorizationHeader = DiscogsAuthHelper.generateOAuthHeaderForInventoryRequest(DiscogsTokens.DISCOG_OAUTH_TOKEN_FOR_USER_ACTION, DiscogsTokens.DISCOG_OAUTH_TOKEN_SECRET_FOR_USER_ACTION);
//        DiscogsUserInventoryResponse userCollectionResponse = discogsClient.getUserInventoryByUserName(authorizationHeader, userName);
//        // List<Listing> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(userCollectionResponse.getListings());
//
//        return userCollectionResponse.getListings();
//    }

    /*******DATABASE SEARCH ENDPOINTS********/

    /***
     * Get all records by catalog number only, without analyzing image.
     * @param catalogNumber
     * @return List<Album> filteredAlbums with metadata
     */
//    public List<Album> getAlbumsByCatalogNumberFromMobile(String catalogNumber) {
//        log.info("received request to getAlbumsByCatalogNumberFromMobile with catNo {}", catalogNumber);
//
//        DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogNumber, DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, DiscogsConstants.COUNTRY, DiscogsConstants.VINYL_FORMAT, "");
//
//        if(discogsSearchResponse.getAlbums().isEmpty()) return new ArrayList<>();
//
//        List<Album> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(discogsSearchResponse.getAlbums());
//        getPriceSuggestions(filteredAlbums);
//
//        return filteredAlbums;
//    }

//    public static String generateOAuthHeaderForIdentityRequest(String oAuthToken, String oAuthTokenSecret) {
//
//        OAuthRequest oAuthRequest = new OAuthRequest();
//
//        String accessTokenAuthHeader =
//                "OAuth oauth_consumer_key=\"" + DiscogsTokens.DISCOGS_CONSUMER_KEY + "\"," +
//                        "oauth_token=\"" + oAuthToken + "\"," +
//                        "oauth_signature_method=\"PLAINTEXT\"," +
//                        "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\"," +
//                        "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\"," +
//                        "oauth_version=\"" + oAuthRequest.getOauth_version() + "\"," +
//                        "oauth_signature=\"" + oAuthRequest.getOauth_signature();
//
//        return accessTokenAuthHeader;
//    }

    /***
     * Get album collection for given userName. Requires authentication as user.
     * @param userName
     * @return
     */
    /****TODO: COLLECTIONS DO NOT INCLUDE ALL THE INFORMATION NEEDED. CAN THIS BE DELETED????*****/
//    public List<Album> getUserCollection(String userName) {
//        log.info("received request to getUserCollection with user name: {}", userName);
//
//        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
//        DiscogsCollectionResponse userCollectionResponse = discogsClient.getUserCollectionByUserName(authHeader, userName);
//
//        List<Album> filteredAlbums = DiscogsSearchResultValidator.filterOutResponseDuplicates(userCollectionResponse.getAlbums());
//
//        return filteredAlbums;
//    }
//    /***
//     * Publish album to uncategorized collection.
//     * @param releaseId
//     * @return HttpStatus
//     */
//    public HttpStatus publishAlbumToUserCollection(String releaseId, int folderId) {
//        log.info("received request to publishAlbum with releaseId {} and folderId {}", releaseId, folderId);
//        // If folderId is not specified, default is 1 (Uncategorized)
//        if(folderId == 0) folderId = 1;
//
//        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
//        HttpStatus publishResponse = discogsClient.uploadAlbumToCollection(authHeader, DiscogsUserCredentials.DISCOGS_USERNAME, folderId, releaseId);
//
//        return publishResponse;
//    }

    /***
     * Get Price Suggestions from Discogs for a single given album.
     * @param releaseId
     * @return
     * @throws FeignException
     */
//    public PriceSuggestionResponse getPriceSuggestions(String releaseId) throws FeignException {
//        log.info("received request to getPriceSuggestions");
//
//        String authHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
//        PriceSuggestionResponse priceSuggestionsResponse = discogsClient.getPriceSuggestions(authHeader, releaseId);
//
//        return priceSuggestionsResponse;
//    }

    /***
     * Get Price Suggestions from Discogs for a given list of albums.
     * @param albums
     * @return List<Album> -- With price suggestions
     */
//    public List<Album> getPriceSuggestions(List<Album> albums) {
//        List<Album> resultsWithPriceSuggestions = new ArrayList<>();
//
//        log.info("Getting price suggestions for {} albums", albums.size());
//
//        for (Album album : albums) {
//            if(!Objects.nonNull(album)) continue;
//            try{
//                TimeUnit.SECONDS.sleep(1);
//
//            }catch (Exception exception) {
//                log.info("Exception sleeping thread {}", exception.getMessage());
//            }
//            try {
//                PriceSuggestionResponse priceSuggestionResponse = getPriceSuggestions(album.getReleaseId());
//                if(priceSuggestionResponse.getGood() != null) album.setAlbumGoodValue(priceSuggestionResponse.getGood().getValue());
//                if(priceSuggestionResponse.getMint() != null) album.setAlbumMintPlusValue(priceSuggestionResponse.getMint().getValue());
//            }
//            catch (FeignException | NullPointerException feignException) {
//                log.info("Exception assigning album value to release {} and catno {} with message\n {}", album.getReleaseId(), album.getCatno(), feignException.getMessage());
//            }
//
//            resultsWithPriceSuggestions.add(album);
//        }
//
//        return resultsWithPriceSuggestions;
//    }

/***************TODO: DELETE OR IMPLEMENT UNUSED METHODS****************/

//    public List<Album> getNextPageOfResults(DiscogsSearchResponse response) {
//        List<Album> allFilteredAlbums = new ArrayList<>();
//        int nextPageNumber = Integer.parseInt(StringHelper.getSubstringParam(response.getPagination().getUrls().get("next"), "&page=", "EnD"));
//        DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, DiscogsConstants.VINYL_FORMAT, 100, nextPageNumber);
//
//        allFilteredAlbums = resp.getAlbums()
//                .stream()
//                .filter(r -> !Objects.equals(r.getCatno(), ""))
//                .map(result -> Album.builder().catno(result.getCatno()).country(result.getCountry()).build())
//                .collect(Collectors.toList());
//
//        return allFilteredAlbums;
//    }
//
//    public String verifyIdentity() {
//        log.info("received request to verify user identity");
//
//        Optional<String> url = DiscogsAuthHelper.getOAuthToken();
//        return url.orElse("");
//    }
//
//    public String getAuthorizationUrl() {
//        log.info("received request to retrieve user authorization URL");
//
//        Optional<String> url = DiscogsAuthHelper.getOAuthToken();
//        return url.orElse("");
//    }
}
