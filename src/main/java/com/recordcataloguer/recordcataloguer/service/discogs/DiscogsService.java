package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

    // TODO: Encode/Decode image from UI, or just save image in cloud/local and pass url?
    // TODO: Add call to price suggestions endpoint
    // TODO: Call to look up image URL and download it/them
    /*****NOTE: DIFFERENT BARCODES EXIST FOR DIFFERENT RELEASES****/
    /***
     * DiscogsService controller method for getting records by an image
     * @param imageUrl
     * @return ResponseEntity of DiscogsResponse
     */
    public ResponseEntity<List<DiscogsResultDTO>> getRecords(String imageUrl) {

        List<String> catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);

        if(catalogueNumbers.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<DiscogsResultDTO> discogsSearchResponse = getRecordsByCatalogueNumber(catalogueNumbers);

        return ResponseEntity.of(Optional.of(discogsSearchResponse));
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

        List<String> extractedCatalogueNumbers = imageReader.extractTextFromImage(url);

        return extractedCatalogueNumbers;
    }

    /***
     * Takes catalogue numbers extracted from image and gets results from Discogs
     * @param catalogueNumbers
     * @return DiscogsSearchResponse
     */
    private List<DiscogsResultDTO> getRecordsByCatalogueNumber(List<String> catalogueNumbers) {

        // ArrayList to hold DiscogsResult objects
        List<DiscogsResultDTO> discogsResultList = new ArrayList<>();
        // For each
            //use catalogue number get Full DiscogsResponse with Pagination Object and List<Results> Objects
                // If Results is not empty
                // Filter Results to NOT include
                // duplicate titles
                // and empty Result
                // build buildDtoFromDiscogsSearchResponse
                // Collect to list, or add to list
        List<List<DiscogsResultDTO>> uiFormattedResponse = new ArrayList<>();

        for (String catalogueNumber : catalogueNumbers) {
            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
            if(discogsSearchResponse.getResults().isEmpty()) return null;//ResponseEntity<>(HttpStatus.NOT_FOUND);
            
            List<Result> results = discogsSearchResponse.getResults();

            List<Result> filteredResults = results.stream()
                    .filter(Objects::nonNull)
                    .filter(distinctByKey(Result::getTitle))
                    .collect(Collectors.toList());

            List<DiscogsResultDTO> discogsResultDTO = discogsSearchResponse.buildDtoFromDiscogsSearchResponse(filteredResults);
            uiFormattedResponse.add(discogsResultDTO);
        }

        // Look up each catalogue number and add to result set
        // Todo: Just add the ones that have results? To avoid using a filter later?
        catalogueNumbers.forEach(catalogueNumber -> discogsResultList
                .add(discogsClient.getUSDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format)));


        // If no results are returned, return 404
        if(discogsResultList.isEmpty()) return null;

        // For each DiscogsResult, filter out duplicate titles and empty responses
        // TODO: Add try catch
        List<DiscogsResultDTO> filteredDiscogsResults = discogsResultList
                .stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(DiscogsResultDTO::getTitle))
                .collect(Collectors.toList());


//        List<DiscogsResult> filteredResults = discogsSearchResultList.get(0).getResults();

//        List<DiscogsSearchResult> filteredDiscogsResults = discogsSearchResultList.stream()
//                .filter(result -> result.)
//                .collect(Collectors.toList());
//
//        List<DiscogsSearchResult> uniqueDiscogsResults = filteredDiscogsResults.stream()
//                .filter(distinctByKey(result -> result.()))
//                .collect(Collectors.toList());

        for (DiscogsResultDTO result: filteredDiscogsResults) {
            log.info("result.title {}", result.getTitle());



        }

        // Todo: Filter
        // DiscogsSearchResponse.Pagination.items > 0 results is empty
        // Duplicate name & title combinations

        return filteredDiscogsResults;
    }

    // Filter distinct by object child prop

    /***
     *
     * @param keyExtractor functional interface that accepts a generic, performs the function (apply method) and returns a generic
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>(); // Map to hold values
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; //
    }

    private DiscogsResultDTO getRecordByCatalogueNumber(String catalogueNumber) {

        DiscogsResultDTO discogsSearchResponse = discogsClient.getUSDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);

        return discogsSearchResponse;
    }

    //    public ResponseEntity<DiscogsSearchResponse> lookupUpRecordByCatalogueNumber(){
//
//        DiscogsSearchResponse discogsSearchResponse =
//                discogsClient.getUSDiscogsRecordsByCategoryNumber(DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, catNo, country);
//        return ResponseEntity.of(Optional.of(discogsSearchResponse));
//    }

    // Make UI consumable object

    // Filter unique artist/title
    // Return first result?
}
