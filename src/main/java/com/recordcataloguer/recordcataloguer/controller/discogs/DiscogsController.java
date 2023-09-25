package com.recordcataloguer.recordcataloguer.controller.discogs;

import com.recordcataloguer.recordcataloguer.constants.LocalHostUrls;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.helpers.image.vision.ImageReader;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsService;
import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsServiceMobile;
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
@CrossOrigin(origins = { LocalHostUrls.LAPTOP_IP, LocalHostUrls.FLUTTER_EMULATOR }) // Allow from Flutter app
public class DiscogsController {

    @Autowired
    private DiscogsService discogsService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ImageReader imageReader;

    @Autowired
    private DiscogsServiceMobile discogsServiceMobile;

    /**************************GET ENDPOINTS*************************/

    @GetMapping(value = "/getAlbumsByCatalogNumberFromMobile")
    public ResponseEntity<List<Album>> getAlbumsByCatalogNumberFromMobile(@RequestParam String catalogNumber) {
        log.debug("Request received to getRecordsByCatNoMobile by catNo: {}", catalogNumber);
        List<Album> albums = discogsServiceMobile.getRecordsByCatalogNumber(catalogNumber);
        log.debug("Response returned with {} albums", albums.size());
        albums.forEach(album -> log.debug("Album: {} {}", album.getTitle(), album.getReleaseId()));
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping(value = "/getRecordsBySpineText")
    public ResponseEntity<List<Album>> getRecordsBySpineText(@RequestParam @NonNull String url, @RequestParam int separatorDistance) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        List<Album> albums = discogsService.getRecordsBySpineText(url, separatorDistance);
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping(value = "/getImageTextForUser")
    public ResponseEntity<List<String>> getImageTextForUser(@RequestParam @NonNull String url, @RequestParam int separatorDistance) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        String results = discogsService.extractTextFromImage(url);
        return new ResponseEntity(discogsService.splitRawText(results), HttpStatus.OK);
    }

    @GetMapping(value = "/getRecordsFromRawText")
    public ResponseEntity<List<String>> getRecordsFromRawText(@RequestParam @NonNull String url, @RequestParam int separatorDistance) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        String results = discogsService.extractTextFromImage(url);
        return new ResponseEntity(discogsService.splitRawText(results), HttpStatus.OK);
    }

    @GetMapping(value = "/getSearchStringsByImageVerticesUrl")
    public ResponseEntity<List<String>> getSearchStringsByImageVerticesUrl(@RequestParam @NonNull String url, @RequestParam String separatorDistance) {

        log.debug("Request received to getSearchStringsByImageVerticesUrl with imageUrl: {}", url);
        List<String> results = discogsService.getSearchStringsByImageVerticesUrl(url, Integer.parseInt(separatorDistance));
        return new ResponseEntity(results, HttpStatus.OK);
    }

    @GetMapping(value = "/getRecordsByRegex")
    public ResponseEntity<List<Album>> getRecordsByRegex(@RequestParam @NonNull String url) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        List<Album> albums = discogsService.getRecordsByRegex(url);
        log.debug("Completed request to get records by regex with {} albums", albums.size());
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllRecords")
    public ResponseEntity<List<String>> getAllRecords() {

        List<AlbumEntity> albums = discogsService.getAllDiscogsCatalogNumbers();
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping(
            value = "/getRecordThumbnailsByImage",
            produces = MediaType.IMAGE_GIF_VALUE
    )
    public ResponseEntity<List<Album>> getRecordThumbnailsByImage(@RequestParam @NonNull String url, @RequestParam int separatorDistance) {
        log.debug("Request received to lookup records from Discogs with imageUrl: {}", url);
        List<Album> albums = discogsService.getRecordsBySpineText(url, separatorDistance);
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    /**************************PUBLISH ENDPOINTS*************************/
    @PostMapping(value = "/publishAlbumUncategorized")
    public ResponseEntity<String> publishAlbum(@RequestParam @NonNull String releaseId, @RequestParam int folderId) {
        log.debug("Request received to publish album with releaseId: {}", releaseId);

        HttpStatus response = discogsServiceMobile.publishAlbumToUserCollection(releaseId, folderId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**************************AUTH ENDPOINTS*************************/
    @GetMapping(value = "/authenticate")
    public String authenticate() {
        log.debug("Request received to authenticate User with Discogs");
        return discogsService.getAuthorizationUrl();
    }

    /**************************EXTRACT TEXT ENDPOINTS*************************/
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
