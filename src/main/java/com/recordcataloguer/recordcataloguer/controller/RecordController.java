package com.recordcataloguer.recordcataloguer.controller;

import com.recordcataloguer.recordcataloguer.service.EbayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = "http://10.116.244.134") // Allow from app
public class RecordController {
    @Autowired
    private EbayService ebayService;

    @GetMapping("/ebay/auth")
    public ResponseEntity<String> getEbayAuthorization(){
        log.debug("Request received for ebay auth");
        return ebayService.getEbayAuthorization();

    }
}
