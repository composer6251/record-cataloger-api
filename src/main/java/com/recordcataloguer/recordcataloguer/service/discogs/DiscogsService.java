package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsResultDTO;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.http.discogs.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    // TODO SOME OF THIS WORK IS NO LONGER NEEDED. JUST RETURN THE URL FOR THE THUMB AND INSIDE THE HTML ELEMENT, SET THE "SRC" Property = THUMB URL
    // TODO: BUT KEEP THE LIST<RESULT> INSTEAD OF THE LIST<MAP<RESULT, STRING>>
    public List<Result> getRecords(String imageUrl) {
        log.info("received request to getRecords");

        List<Result> discogsSearchResponse = getRecordsByImageText(imageUrl);

        return discogsSearchResponse;
    }

    /*****NOTE: DIFFERENT BARCODES EXIST FOR DIFFERENT RELEASES****/
    /***
     * DiscogsService controller method for getting records by an image
     * @param imageUrl
     * @return ResponseEntity of DiscogsResponse
     */
    private List<Result> getRecordsByImageText(String imageUrl) {

        List<String> catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);

        if(catalogueNumbers.isEmpty()) return null;

        List<Result> results = getRecordsByCatalogueNumber(catalogueNumbers);

        return results;
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

        List<Result> results = new ArrayList<>();
        // TODO: Try to make into lambda
        // Filter out empty/null
        // Filter out duplicates by title? Isn't there Distinct on lambda?
        // Filter duplicates outside of loop
        for (String catalogueNumber : catalogueNumbers) {

            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
            if(discogsSearchResponse.getResults().isEmpty()) continue;

            results.addAll(discogsSearchResponse.getResults());
        }

        List<Result> allFilteredResults = results
                .stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Result::getTitle))
                .collect(Collectors.toList());

        return allFilteredResults;
    }

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

    // TODO: Method to lookup by price, and get average? Or lookup by Discogs endpoint for price suggestion.
}
