package com.recordcataloguer.recordcataloguer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "feignclienttest", url = EbayUrls.EBAY_SANDBOX_BASE_URL)
public interface EbaySandboxClient {

    //todo: Configure ebay request
    @PostMapping(path = EbayUrls.SANDBOX_AUTHORIZATION_API_PATH)
    ResponseEntity<String> getEbaySandboxAuthorization(String uri);
}
