package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.helpers.string.StringHelper;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.http.discogs.PriceSuggestionResponse;
import com.recordcataloguer.recordcataloguer.http.discogs.Result;
import com.recordcataloguer.recordcataloguer.helpers.auth.AuthHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    // TODO: Defaulting to "US" to minimize results/duplicates. User should have option to search everywhere on UI
    private String country = "US";
    private final String format = "vinyl";

    private final String token = DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN;

    // Use endpoint for record lookup based on image
    // Inside service method that gets records, make another call based on release number
    // Create model for suggestedPrice response. Can it just be the field for suggested price? Look at the response from Discogs
    // For each of those, get suggested prices
    // Add suggested prices to Result object
    // Add property suggestedPrice to Result object
    // Return Result
    // In UI, div for suggested Price

    public String uploadRelease() {
        log.info("received request to retrieve user authorization URL");
        // Get Token
        String authHeader = AuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        discogsClient.uploadAlbumReleaseToUncategorizedCollection(authHeader, "dfennell31@gmail.com", "1", "placeholder");
        return authHeader;
    }

    public PriceSuggestionResponse getPriceSuggestions(int releaseId) {
        log.info("received request to getPriceSuggestions");
        // Get Token
        String authHeader = AuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
//        String releaseId = StringHelper.getReleaseId(url);
        log.info("Sending request to getPrice Sugge3stinos {} and {}", authHeader, releaseId);
        PriceSuggestionResponse priceSuggestionsResponse = discogsClient.getPriceSuggestions(authHeader, releaseId);
        return priceSuggestionsResponse;
    }

    public String verifyIdentity() {
        log.info("received request to retrieve user authorization URL");
        // Get Token
        Optional<String> url = AuthHelper.getOAuthToken();
        return url.orElse("");
    }

    public List<Result> getRecords(String imageUrl) {
        log.info("received request to getRecords");

        List<Result> discogsSearchResponse = getRecordsByImageText(imageUrl);

        return discogsSearchResponse;
    }

    public String getAuthorizationUrl() {
        log.info("received request to retrieve user authorization URL");
        // Get Token
        Optional<String> url = AuthHelper.getOAuthToken();
        return url.orElse("");
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

        // Get all records from discogs

        // List<String> for all catalogue numbers

        //
        for (String catalogueNumber : catalogueNumbers) {

            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
            if(discogsSearchResponse.getResults().isEmpty()) continue;
            for (Result result: discogsSearchResponse.getResults()) {
                result.setCatalogNumberForLookup(catalogueNumber);
            }
            results.addAll(discogsSearchResponse.getResults());
        }

        List<Result> allFilteredResults = results
                .stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Result::getTitle))
                .collect(Collectors.toList());

        List<Result> resultsWithPriceSuggestions = new ArrayList<>();
        for (Result result: allFilteredResults) {
            if(!Objects.nonNull(result)) continue;

            PriceSuggestionResponse priceSuggestionResponse = getPriceSuggestions(result.getId());

            try {
                if(priceSuggestionResponse.getGood() != null) result.setAlbumGoodValue(priceSuggestionResponse.getGood().getValue());
                if(priceSuggestionResponse.getMint() != null) result.setAlbumMintPlusValue(priceSuggestionResponse.getMint().getValue());
            }
            catch (NullPointerException exception) {
                log.info("Exception assiging album value to release {} and catno {} with message\n {}", result.getId(), result.getCatno(), exception.getMessage());
            }

            resultsWithPriceSuggestions.add(result);
        }

        return resultsWithPriceSuggestions;
    }

    @SneakyThrows
    public List<Result> getAllDiscogsCatalogNumbers() {

        // Get a list of all discogs catalogue numbers

        // Maybe see if there's are patterns in order of text on album spine

        // Create multiple regexs
        // "abc-123" if found move on, else do 2 then 3
        // "abc 123"
        // "Captal records 9000
        // Maybe extract label and use it? Need list of labels?

        // Check for Name?

        // More work for user? Have them start with definitive catnos?

        // Option for release year time frame?
//        List<Result>
        // Ask Tony about:
        //      1. Scheduling a meeting to discuss code
        //      2. Framework
         //     3. How many apps
        //      4. Only back end Java or front end work too

        //
        //      1. Staying on 4 10s
        //      2. WFH if wife travels
        //      3. Part-time position
        //      4. Does the project on my timesheet change?
        //      5. Does Stew still approve my timesheets?

        DiscogsSearchResponse response = discogsClient.getAllDiscogsCatalogNumbers(token, format, 100);
        List<Result> resultsToReturn = new ArrayList<>();

        int i = 1;
        while(response.getPagination() != null && response.getPagination().getUrls().containsKey("next") && i < 101) {
            if(i % 20 == 0){
                log.info("Pausing requests for 1 minute. result size {}", resultsToReturn.size());
                TimeUnit.MINUTES.sleep(1);
            }
            DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(token, format, 100, i++);

            List<Result> allFilteredResults = resp.getResults()
                    .stream()
                    .filter(r -> !Objects.equals(r.getCatno(), ""))
                    .map(result -> Result.builder().catno(result.getCatno()).country(result.getCountry()).title(result.getTitle()).build())
                    .collect(Collectors.toList());

            resultsToReturn.addAll(allFilteredResults);
            log.info("resultsToReturn");

        }

        resultsToReturn.forEach(result -> System.out.println(result.getCountry() + " " + result.getCatno() + " " + result.getTitle()));
        return resultsToReturn;
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(response.getPagination().getUrls().get("next")))
//                    .header("Content-Type", "application/x-www-form-urlencoded")
//                    .build();
//
//            HttpResponse resp = HttpHelper.sendRequest(request);
//            log.info("resp");
//            DiscogsSearchResponse res = (DiscogsSearchResponse) resp.body();
//            log.info("res");

        // While response.pagination.urls.next
        // Filter List<Result> and add the filteredList
        // Add country, year to object

        // Once complete:
        // Sort by country, catno format?
        // Or write to file/spreadsheet?


    }

    public List<Result> getNextPageOfResults(DiscogsSearchResponse response) {
        List<Result> allFilteredResults = new ArrayList<>();
            int nextPageNumber = Integer.parseInt(StringHelper.getSubstringParam(response.getPagination().getUrls().get("next"), "&page=", "EnD"));
            DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(token, format, 100, nextPageNumber);

            allFilteredResults = resp.getResults()
                    .stream()
                    .filter(r -> !Objects.equals(r.getCatno(), ""))
                    .map(result -> Result.builder().catno(result.getCatno()).country(result.getCountry()).build())
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
