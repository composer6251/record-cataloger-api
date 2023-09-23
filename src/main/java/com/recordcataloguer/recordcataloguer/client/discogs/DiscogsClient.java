package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.PriceSuggestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "discogsService", url = DiscogsUrls.DISCOGS_API_BASE_URL)
public interface DiscogsClient {

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

    ///users/{username}/collection/folders/{folder_id}/releases/{release_id}
    @PostMapping(value = DiscogsUrls.USER_COLLECTION_API + "/{username}/collection/folders/{folder_id}/releases/{release_id}", consumes = "application/x-www-form-urlencoded")
    HttpStatus uploadAlbumToCollection(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username,
            @PathVariable("folder_id") int folder_id,
            @PathVariable("release_id") String release_id
    );

    @GetMapping(value = DiscogsUrls.MARKETPLACE_API + DiscogsUrls.PRICE_SUGGESTIONS_ENDPOINT + "/{release_id}", consumes = "application/x-www-form-urlencoded")
    PriceSuggestionResponse getPriceSuggestions(
            @RequestHeader("Authorization") String auth,
            @PathVariable("release_id") String release_id
    );
}
