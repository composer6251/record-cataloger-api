package com.recordcataloguer.recordcataloguer.controller;

import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.service.EbayService;
import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

@RestController
@Slf4j
@CrossOrigin(origins = "http://10.116.244.134") // Allow from Flutter app
public class DiscogsController {

    @Autowired
    private DiscogsService discogsService;

    @GetMapping("/extractTextFromImage")
    public ModelAndView extractTextFromImage(@PathVariable String url)
            throws URISyntaxException, IOException, InterruptedException {
        log.debug("Request received for text extraction");

        ImageReader imageReader = new ImageReader();
        ModelAndView modelAndView;

        modelAndView = imageReader.extractTextFromImage(url);

        return modelAndView;
    }

    @GetMapping("/lookupRecord")
    public ModelAndView lookUpRecordByCategoryNumber(@RequestBody String url)
            throws URISyntaxException, IOException, InterruptedException {
        log.debug("Request received for text extraction");

        ImageReader imageReader = new ImageReader();
        ModelAndView modelAndView;

        modelAndView = imageReader.extractTextFromImage(url);

        return modelAndView;
    }

    @GetMapping("/lookupRecordByUrl")
    public ModelAndView lookUpRecordByCategoryNumberParam(@RequestParam String url)
            throws URISyntaxException, IOException, InterruptedException {
        log.debug("Request received for text extraction");

        ImageReader imageReader = new ImageReader();
        ModelAndView modelAndView;

        modelAndView = imageReader.extractTextFromImage(url);

        return modelAndView;
    }
}
