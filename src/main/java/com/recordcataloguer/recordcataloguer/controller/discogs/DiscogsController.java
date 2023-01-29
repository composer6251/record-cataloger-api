package com.recordcataloguer.recordcataloguer.controller.discogs;

import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://10.116.244.134", "http://localhost:3000"}) // Allow from Flutter app
public class DiscogsController {

    @Autowired
    private DiscogsService discogsService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ImageReader imageReader;

    @GetMapping(value = "/getRecords")
    public ResponseEntity<List<Album>> getRecords(@RequestParam @NonNull String url) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        List<Album> albums = discogsService.getRecords(url);
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllRecords")
    public ResponseEntity<List<String>> getAllRecords() {

        List<Album> albums = discogsService.getAllDiscogsCatalogNumbers();
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping(value = "/authenticate")
    public String authenticate() {
        log.debug("Request received to authenticate User with Discogs");
        return discogsService.getAuthorizationUrl();
    }

    @GetMapping(
            value = "/getRecordThumbnailsByImage",
            produces = MediaType.IMAGE_GIF_VALUE
    )
    public ResponseEntity<List<Album>> getRecordThumbnailsByImage(@RequestParam @NonNull String url) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        List<Album> albums = discogsService.getRecords(url);
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping("/extractTextFromImage")
    public String extractTextFromImage(@RequestParam @NonNull String url) {
        log.error("Request received to lookup records from Discogs with imageUrl: {}", url);

        return discogsService.extractTextFromImage(url);
    }

    @GetMapping("/extractLabelsFromImage")
    public ResponseEntity<List<String>> extractLabelsFromImage(@PathVariable String url) {

        log.debug("Request received for text extraction");

        List<String> catalogueNumbers = imageReader.extractLabelFromImage(url);

        return ResponseEntity.of(Optional.of(catalogueNumbers));
    }
}
