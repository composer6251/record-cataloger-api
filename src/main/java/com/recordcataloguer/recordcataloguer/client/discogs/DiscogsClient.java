package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.config.FeignConfiguration;
import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.PriceSuggestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "discogsService", url = DiscogsUrls.DISCOGS_API_BASE_URL, configuration = FeignConfiguration.class)
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


    /****TODO:
     * 1. Regex to get release ID
     * 2. Get priceSuggestion by releaseID
     *      a. create Request/Response objects
     *      b. create client method in feign
     *      c. test request with postman
     *      d. default to uncategorized
     * 3. Return to front end
     *      a. create controller endpoint
     *      b. create service method
     *      c. build request with auth tokens
     * 4. in UI
     *      a. Display price suggestion
     *      b. Based on what? good condition?
     *
     *
     * 5. Add way to determine if album is actual duplicate
     *      a. apache getCommonPrefix????
     * ***/

    ///users/{username}/collection/folders/{folder_id}/releases/{release_id}
    @PostMapping(value = DiscogsUrls.USER_COLLECTION_API + "/{username}/collection/folders/{folder_id}/releases/{release_id}", consumes = "application/x-www-form-urlencoded")
    DiscogsSearchResponse uploadAlbumReleaseToUncategorizedCollection(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username,
            @PathVariable("folder_id") String folder_id,
            @PathVariable("release_id") String release_id
    );

    @GetMapping(value = DiscogsUrls.MARKETPLACE_API + DiscogsUrls.PRICE_SUGGESTIONS_ENDPOINT + "/{release_id}", consumes = "application/x-www-form-urlencoded")
    PriceSuggestionResponse getPriceSuggestions(
            @RequestHeader("Authorization") String auth,
            @PathVariable("release_id") int release_id
    );
}
