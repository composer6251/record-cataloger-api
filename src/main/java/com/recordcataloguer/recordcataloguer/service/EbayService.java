package com.recordcataloguer.recordcataloguer.service;

import com.recordcataloguer.recordcataloguer.client.ebay.EbaySandboxClient;
import com.recordcataloguer.recordcataloguer.constants.auth.ebay.EbayUrls;
import com.recordcataloguer.recordcataloguer.helpers.image.encode.ImageEncodingHelper;
import com.recordcataloguer.recordcataloguer.helpers.httpclienthelper.HttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/***
 * Ebay Service for interacting with the Ebay Feign client
 */
@Service
@Slf4j
public class EbayService {
    @Autowired
    private EbaySandboxClient ebayClient;

    private String ebayAuthorizationToken = "";

    private String contentType = "application/x-www-form-urlencoded";
    private String authorization = "Basic " + EbayUrls.ENCODED_CLIENT_ID_SECRET;
    private String grant = "client_credentials";

    private static final String filePath = "/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/20221017_163141.jpg";


    // todo: Determine if this still works
    public HttpStatus getEbayAuthorization() throws IOException {
        String binaryOfEncodedImage = ImageEncodingHelper.encodeImage(filePath, "");
        ImageEncodingHelper.decodeImage(binaryOfEncodedImage, "/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/test");
        ImageEncodingHelper.decodeImage(binaryOfEncodedImage, "/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/testPicture.jpg");

        log.info("Sending ebay authorization request");
        ResponseEntity<String> authorizationResponse = ebayClient.getEbaySandboxAuthorization(contentType, authorization, grant);
        log.info("Received ebay authorization response with status code {} and response {}", authorizationResponse.getStatusCode(), authorizationResponse.getBody());
        if (authorizationResponse.getStatusCode().is2xxSuccessful()) {
            ebayAuthorizationToken = authorizationResponse.getBody();
        }
        return authorizationResponse.getStatusCode();
    }

    public HttpResponse searchAlbumByArtistAndNameHttp(String artist, String name)
            throws URISyntaxException, IOException, InterruptedException {
        if(ebayAuthorizationToken.isEmpty()) {
            final ResponseEntity<String> authorization = ebayClient.getEbaySandboxAuthorization(contentType, this.authorization, grant);
            this.ebayAuthorizationToken = authorization.getBody();
            log.info("Auth Token from Album Search: " + authorization.getBody());
        }

        HttpRequest request;
        request = HttpHelper.generateRequest(EbayUrls.EBAY_SANDBOX_BASE_URL,ebayAuthorizationToken);
        HttpResponse response = HttpHelper.sendRequest(request);
        log.info("Response from album search {}", response.body().toString());

        return response;
    }
}
