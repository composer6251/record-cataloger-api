package com.recordcataloguer.recordcataloguer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name="feign-client", url = EbayUrls.EBAY_SANDBOX_BASE_URL)
public interface EbaySandboxClient {

    //todo: Configure ebay request
    @PostMapping(path = EbayUrls.SANDBOX_AUTHORIZATION_API_PATH, value = EbayUrls.SANDBOX_AUTHORIZATION_API_PATH)
    ResponseEntity<String> getEbaySandboxAuthorization(@RequestHeader Map<String, String> headers, @RequestBody Map<String, String> body);
}
