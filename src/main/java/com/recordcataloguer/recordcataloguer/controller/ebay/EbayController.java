package com.recordcataloguer.recordcataloguer.controller.ebay;

import com.recordcataloguer.recordcataloguer.constants.LocalHostUrls;
import com.recordcataloguer.recordcataloguer.service.EbayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

@RestController
@Slf4j
@CrossOrigin(origins = LocalHostUrls.LAPTOP_IP) // Allow from app
public class EbayController {
    @Autowired
    private EbayService ebayService;

    @GetMapping("/ebay/auth")
    public HttpStatus getEbayAuthorization() throws IOException {
        log.debug("Request received for ebay auth");

        return ebayService.getEbayAuthorization();

    }

    @GetMapping("/ebay/searchByArtistAndAlbum")
    public HttpResponse searchByArtistAndAlbum()
            throws URISyntaxException, IOException, InterruptedException {
        log.debug("Request received for ebay search by artist and album");

        return ebayService.searchAlbumByArtistAndNameHttp("CatStevens", "name");
    }
}