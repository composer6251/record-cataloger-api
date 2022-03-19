package com.recordcataloguer.recordcataloguer.service;

import com.recordcataloguer.recordcataloguer.client.EbaySandboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EbayService {
    private EbaySandboxClient ebayClient;

    // todo: Pass necessary request
    public ResponseEntity<String> getEbayAuthorization(){

        log.debug("Sending request to get ebay sandbox auth");
        return ebayClient.getEbaySandboxAuthorization("url");
    }

}
