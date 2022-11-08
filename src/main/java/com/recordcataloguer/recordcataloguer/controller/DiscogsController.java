package com.recordcataloguer.recordcataloguer.controller;

import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.service.EbayService;
import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "http://10.116.244.134") // Allow from Flutter app
public class DiscogsController {

    @Autowired
    private DiscogsService discogsService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ImageReader imageReader;

    // Take request for record look up by image

    // Image Reader extracts text

    // ImageReaderUtil filters catalogue numbers

    // If none empty, For each catalogue number, call discogs for result

    // Maybe get barcode?

    // Maybe look up again?

    // Or return all results and let user choose?
    // This could be ALOT of results


    @GetMapping("/lookupRecords")
    public ResponseEntity<List<DiscogsSearchResponse>> lookUpRecords(@RequestParam @NonNull String url) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);

        return discogsService.getRecords(url);
    }

    @GetMapping("/extractLabelsFromImage")
    public ResponseEntity<List<String>> extractLabelsFromImage(@PathVariable String url) {

        log.debug("Request received for text extraction");

        List<String> catalogueNumbers = imageReader.extractLabelFromImage(url);

        return ResponseEntity.of(Optional.of(catalogueNumbers));
    }

//    @GetMapping("/lookupRecordByUrl")
//    public ModelAndView lookUpRecordByCategoryNumberParam(@RequestParam String url)
//            throws URISyntaxException, IOException, InterruptedException {
//        log.debug("Request received for text extraction");
//
//        ModelAndView modelAndView;
//
//        modelAndView = imageReader.extractTextFromImage(url);
//
//        return modelAndView;
//    }
}
