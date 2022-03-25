package com.recordcataloguer.recordcataloguer.service;

import com.recordcataloguer.recordcataloguer.client.EbaySandboxClient;
import com.recordcataloguer.recordcataloguer.client.EbayUrls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EbayService {
    @Autowired
    private EbaySandboxClient ebayClient;

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> body = new HashMap<>();

    // todo: Pass necessary request
    public ResponseEntity<String> getEbayAuthorization(){

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", "Basic " + "RGF2aWRGZW4tcmVjb3JkY2EtU0JYLTk2YWUyZDY1Yy1kNDRlNTcxNzpTQlgtNmFlMmQ2NWMyZjIzLTFhYjctNDVjOS04YWM4LTYwNDg=");
        body.put("grant_type", "client_credentials");

        log.debug("Sending request to get ebay sandbox auth");
        return ebayClient.getEbaySandboxAuthorization(headers, body);
    }

}
