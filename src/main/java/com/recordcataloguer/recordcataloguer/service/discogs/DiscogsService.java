package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.helpers.encode.ImageEncodingHelper;
import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsResultDTO;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.http.discogs.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DiscogsService {

    @Autowired
    private DiscogsClient discogsClient;

    @Autowired
    private ImageReader imageReader;

    // Defaulting to "US" to minimize results/duplicates. User should have option to search everywhere on UI
    private String country = "US";
    private final String format = "vinyl";

    private final String token = DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN;

    private static Result copyResultWithEncodedThumb(Result record) {
        Result resultWithImage =  new Result(record);

        resultWithImage.setEncodedThumb(ImageEncodingHelper.getEncodedImageFromDiscogs(record.getThumb()));

        return resultWithImage;
    }

    // TODO: Encode/Decode image from UI, or just save image in cloud/local and pass url?
    // TODO: Add call to price suggestions endpoint
    // TODO: Call to look up image URL and download it/them

    /***
     * Extract possible cat numbers
     * Discogs API for result
     * Model should only grab the image thumb
     */
    // TODO SOME OF THIS WORK IS NO LONGER NEEDED. JUST RETURN THE URL FOR THE THUMB AND INSIDE THE HTML ELEMENT, SET THE "SRC" Property = THUMB URL
    // TODO: BUT KEEP THE LIST<RESULT> INSTEAD OF THE LIST<MAP<RESULT, STRING>>
    public List<Result> getRecordsByImageText(String imageUrl) {

        List<Result> discogsSearchResponse = getRecords(imageUrl);

        // Create new object list with encodedThumbs for thumbnails
//        List<Result> thumbnails = discogsSearchResponse.stream()
//                .filter(record -> record.getThumb() != null)
////                .map(DiscogsService::copyResultWithEncodedThumb)
//                .collect(Collectors.toList());

        return discogsSearchResponse;
    }

//    public ResponseEntity<List<Map<String, Result>>> getRecordsAsJSON(String imageUrl) {
//
//        List<Map<String, Result>> discogsSearchResponse = getRecords(imageUrl);
//
//        return ResponseEntity.of(Optional.of(discogsSearchResponse));
//    }

    /*****NOTE: DIFFERENT BARCODES EXIST FOR DIFFERENT RELEASES****/
    /***
     * DiscogsService controller method for getting records by an image
     * @param imageUrl
     * @return ResponseEntity of DiscogsResponse
     */
//    private List<Map<String, Result>> getRecords(String imageUrl) {
//
//        List<String> catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);
//
//        if(catalogueNumbers.isEmpty()) return null;
//
//        List<Map<String, Result>> results = getRecordsByCatalogueNumber(catalogueNumbers);
//        //List<List<DiscogsResultDTO>> discogsSearchResponse = getRecordsByCatalogueNumber(catalogueNumbers);
//
//        return results;
//    }
    private List<Result> getRecords(String imageUrl) {

        List<String> catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);

        if(catalogueNumbers.isEmpty()) return null;

        List<Result> results = getRecordsByCatalogueNumber(catalogueNumbers);

        return results;
    }



    public ResponseEntity<DiscogsResultDTO> getRecord(String imageUrl) {

        List<String> catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);

        if(catalogueNumbers.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        DiscogsResultDTO discogsResult = getRecordByCatalogueNumber(catalogueNumbers.get(0));

        return ResponseEntity.of(Optional.of(discogsResult));
    }

    /***
     * Gets catalogueNumbers from the image by filtering out format determined by regex
     * @param url
     * @return extractedCatalogueNumbers
     */
    private List<String> extractCatalogueNumbersFromImage(String url){

        List<String> extractedCatalogueNumbers = imageReader.extractCatalogueNumberFromImage(url);

        return extractedCatalogueNumbers;
    }

    public String extractTextFromImage(String url){

        String extractedCatalogueNumbers = imageReader.extractTextFromImage(url);

        return extractedCatalogueNumbers;
    }

    /***
     * Takes catalogue numbers extracted from image and gets results from Discogs
     * @param catalogueNumbers
     * @return DiscogsSearchResponse
     */
    private List<Result> getRecordsByCatalogueNumber(List<String> catalogueNumbers) {

        // List of key value pairs. Key = record value = result information
        List<Result> allFilteredResults = new ArrayList<>();

        for (String catalogueNumber : catalogueNumbers) {
            Map<String, Result> filteredResultMap = new HashMap<>();

            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
            if(discogsSearchResponse.getResults().isEmpty()) continue;
            
            List<Result> results = discogsSearchResponse.getResults();

            return results;
        }

        return allFilteredResults;
    }
//    private List<Map<String, Result>> getRecordsByCatalogueNumber(List<String> catalogueNumbers) {
//
//        // List of key value pairs. Key = record value = result information
//        List<Map<String, Result>> allFilteredResults = new ArrayList<>();
//
//        for (String catalogueNumber : catalogueNumbers) {
//            Map<String, Result> filteredResultMap = new HashMap<>();
//
//            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
//            if(discogsSearchResponse.getResults().isEmpty()) continue;
//
//            List<Result> results = discogsSearchResponse.getResults();
//            // check if object is not null
//            // if not, putIfAbsent in map as <title, result>
//            for (Result result : results) {
//                if(Objects.nonNull(result)){
//                    filteredResultMap.putIfAbsent(result.getTitle(), result);
//                }
//            }
//            allFilteredResults.add(filteredResultMap);
//        }
//
//        return allFilteredResults;
//    }

    // For each catalogue Number, call getRecordByCatalogueNumber
    private DiscogsResultDTO getRecordByCatalogueNumber(String catalogueNumber) {

        DiscogsResultDTO discogsSearchResponse = discogsClient.getUSDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
        return discogsSearchResponse;
    }

    //    private List<Map<String, Result>> filter

    // TODO: Method to lookup by price, and get average? Or lookup by Discogs endpoint for price suggestion.
}
