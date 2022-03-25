package com.recordcataloguer.recordcataloguer.controller;

import com.recordcataloguer.recordcataloguer.service.EbayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = "http://10.116.244.134") // Allow from app
public class RecordController {
    @Autowired
    private EbayService ebayService;

    @GetMapping("/ebay/auth")
    public HttpStatus getEbayAuthorization(){
        log.debug("Request received for ebay auth");
        return ebayService.getEbayAuthorization();

    }

//    @GetMapping("/ebay/searchByArtistAndAlbum")
//    public ResponseEntity<String> searchByArtistAndAlbum(){
//        log.debug("Request received for ebay search by artist and album");
//        return ebayService.searchAlbumByArtistAndName("CatStevens");
//    }
}
