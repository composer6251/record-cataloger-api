package com.recordcataloguer.recordcataloguer.controller;

import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.service.Discog.DiscogService;
import com.recordcataloguer.recordcataloguer.service.EbayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

@RestController
@Slf4j
@CrossOrigin(origins = "http://10.116.244.134") // Allow from app
public class RecordController {
    @Autowired
    private EbayService ebayService;

    @Autowired
    private DiscogService discogService;

    @GetMapping("/extractTextFromImage")
    public ModelAndView extractTextFromImage(@PathVariable String url)
            throws URISyntaxException, IOException, InterruptedException {
        log.debug("Request received for text extraction");

        ImageReader imageReader = new ImageReader();
        ModelAndView modelAndView;

        modelAndView = imageReader.extractTextFromImage(url);

        return modelAndView;
    }

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
