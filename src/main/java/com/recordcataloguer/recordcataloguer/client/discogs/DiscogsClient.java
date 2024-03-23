package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.DiscogsCollectionResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.UserCollectionByFolderResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.marketplaceapi.DiscogsUserInventoryResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.PriceSuggestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "discogsService", url = DiscogsUrls.DISCOGS_API_BASE_URL)
public interface DiscogsClient {



    /***********SEARCH ALBUMS CALLS**********/
    @GetMapping(value = DiscogsUrls.DATABASE_API + DiscogsUrls.SEARCH_ENDPOINT, consumes = "application/json")
    DiscogsSearchResponse getDiscogsRecordByCategoryNumber(
            @RequestParam("catno") String catalogueNumber,
            @RequestParam("token") String token,
            @RequestParam("country") String country,
            @RequestParam("format") String format,
            @RequestParam("title") String title
    );

    @GetMapping(value = DiscogsUrls.DATABASE_API + DiscogsUrls.SEARCH_ENDPOINT, consumes = "application/json")
    DiscogsSearchResponse getDiscogsRecordByQueryString(
            @RequestParam("query") String query,
            @RequestParam("token") String token,
            @RequestParam("country") String country,
            @RequestParam("format") String format
    );

    @GetMapping(value = DiscogsUrls.DATABASE_API + DiscogsUrls.SEARCH_ENDPOINT, consumes = "application/json")
    DiscogsSearchResponse getDiscogsRecordByTitle(
            @RequestParam("title") String title,
            @RequestParam("token") String token,
            @RequestParam("country") String country,
            @RequestParam("format") String format
            );


    @GetMapping(value = DiscogsUrls.DATABASE_API + DiscogsUrls.SEARCH_ENDPOINT, consumes = "application/json")
    DiscogsSearchResponse getAllDiscogsCatalogNumbers(
            @RequestParam("token") String token,
            @RequestParam("format") String format,
            @RequestParam("per_page") int perPage
    );

    @GetMapping(value = DiscogsUrls.DATABASE_API + DiscogsUrls.SEARCH_ENDPOINT, consumes = "application/json")
    DiscogsSearchResponse getNextDiscogsSearchResultPage(
            @RequestParam("token") String token,
            @RequestParam("format") String format,
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int nextPage
    );

    /*********USER INVENTORY CALLS*******/
    @GetMapping(value = "/users/{username}/inventory", consumes = "application/x-www-form-urlencoded")
    DiscogsUserInventoryResponse getUserInventoryByUserName(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username
    );

    @GetMapping(value = "/users/{username}/inventory", consumes = "application/x-www-form-urlencoded")
    DiscogsUserInventoryResponse getUserInventoryByUserNameAndToken(
            @RequestParam("token") String token,
            @PathVariable("username") String username
    );

    /*********USER COLLECTION CALLS******/
    @GetMapping(value = DiscogsUrls.USER_COLLECTION_API + "/{username}/collection/folders", consumes = "application/x-www-form-urlencoded")
    DiscogsCollectionResponse getUserCollectionByUserName(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username
    );

    @GetMapping(value = DiscogsUrls.USER_COLLECTION_API + "/{username}/collection/folders/{folder_id}/releases", consumes = "application/x-www-form-urlencoded")
    UserCollectionByFolderResponse getCollectionReleasesByFolderId(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username,
            @PathVariable("folder_id") int folderId
    );

    @PostMapping(value = DiscogsUrls.USER_COLLECTION_API + "/{username}/collection/folders/{folder_id}/releases/{release_id}", consumes = "application/x-www-form-urlencoded")
    HttpStatus uploadAlbumToCollection(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username,
            @PathVariable("folder_id") int folderId,
            @PathVariable("release_id") String release_id
    );

    /********PRICING CALLS*******/
    @GetMapping(value = DiscogsUrls.MARKETPLACE_API + DiscogsUrls.PRICE_SUGGESTIONS_ENDPOINT + "/{release_id}", consumes = "application/x-www-form-urlencoded")
    PriceSuggestionResponse getPriceSuggestions(
            @RequestHeader("Authorization") String auth,
            @PathVariable("release_id") String release_id
    );
}
