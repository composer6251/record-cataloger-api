package com.recordcataloguer.recordcataloguer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "ebayService", url = EbayUrls.EBAY_SANDBOX_BASE_URL)
public interface EbaySandboxClient {

    //todo: Configure ebay request
    @PostMapping(value = EbayUrls.SANDBOX_AUTHORIZATION_API_PATH, consumes = "application/json")
    ResponseEntity<String> getEbaySandboxAuthorization(
            @RequestHeader("Content-Type") String contentType,
            @RequestHeader("Authorization") String authorization,
            @RequestParam("grant_type") String grant_type
    );

//    @GetMapping(value = EbayUrls.CATALOG_API_SEARCH_PATH, consumes = "application/json")
//    ResponseEntity<String> searchAlbumByArtistAndAlbumName(
//            @RequestHeader("Content-Type") String contentType,
//            @RequestHeader("Authorization") String authorization,
//            @RequestParam("q") String q
//
//    );
}
