package com.recordcataloguer.recordcataloguer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@Service
@FeignClient(value = "feignclienttest", url = "localhost:8085")
public interface EbayClient {

}
