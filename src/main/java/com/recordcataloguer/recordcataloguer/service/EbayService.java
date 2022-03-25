package com.recordcataloguer.recordcataloguer.service;

import com.recordcataloguer.recordcataloguer.client.EbaySandboxClient;
import com.recordcataloguer.recordcataloguer.client.EbayUrls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EbayService {
    @Autowired
    private EbaySandboxClient ebayClient;

    private String ebayAuthorizationToken = "";

    private String contentType = "application/x-www-form-urlencoded";
    private String authorization = "Basic " + EbayUrls.ENCODED_CLIENT_ID_SECRET;
    private String grant = "client_credentials";

    // todo: Pass necessary request
    public HttpStatus getEbayAuthorization(){
        log.info("Sending ebay authorization request");
        ResponseEntity<String> authorizationResponse = ebayClient.getEbaySandboxAuthorization(contentType, authorization, grant);
        log.info("Received ebay authorization response with status code {}", authorizationResponse.getStatusCode());
        if (authorizationResponse.getStatusCode().is2xxSuccessful()) {
            ebayAuthorizationToken = authorizationResponse.getBody();
        }
        return authorizationResponse.getStatusCode();
    }
//
//    public ResponseEntity<String> searchAlbumByArtistAndName(String artist, String name) {
//        return ebayClient.searchAlbumByArtistAndAlbumName();
//    }

}
