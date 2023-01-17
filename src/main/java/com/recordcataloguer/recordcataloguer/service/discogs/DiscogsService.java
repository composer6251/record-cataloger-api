package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsNewTokens;
import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.helpers.http.HttpHelper;
import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsResultDTO;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.http.discogs.OAuth;
import com.recordcataloguer.recordcataloguer.http.discogs.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    // TODO SOME OF THIS WORK IS NO LONGER NEEDED. JUST RETURN THE URL FOR THE THUMB AND INSIDE THE HTML ELEMENT, SET THE "SRC" Property = THUMB URL
    // TODO: BUT KEEP THE LIST<RESULT> INSTEAD OF THE LIST<MAP<RESULT, STRING>>
    public void authenticate() {
        log.info("received request to authenticate");

//        String result = discogsClient.requestToken(new OAuth(), "Reccord-Catalogue/1.0");

//        OAuth authObj = new OAuth();
//        String test = "OAuth oauth_consumer_key=\"jzTcIyulkcEoOTEZLSmU\",oauth_token=\"EWVAPiForTZCsgRTwOrMJPdQqcGixHscVPaeagAb\"," +
//                "oauth_signature_method=\"PLAINTEXT\",oauth_timestamp=\"1673959108\",oauth_nonce=\"ce61f359-3403-482c-a31c-8e4df85a2301\"," +
//                "oauth_version=\"1.0\",oauth_signature=\"rwYTcKOKqssuGwbwNHlpkMYpspJuYbEC%26\"";
//        String test2 = "OAuth oauth_consumer_key=\"jzTcIyulkcEoOTEZLSmU\",oauth_token=\"EWVAPiForTZCsgRTwOrMJPdQqcGixHscVPaeagAb\"," +
//                "oauth_signature_method=\"PLAINTEXT\",oauth_timestamp=\"1673959108\",oauth_nonce=\"ce61f359-3403-482c-a31c-8e4df85a2301\"," +
//                "oauth_version=\"1.0\",oauth_signature=\"rwYTcKOKqssuGwbwNHlpkMYpspJuYbEC%26\"";
//        String authString = "OAuth oauth_consumer_key=" + authObj.getOauth_consumer_key() + "oauth_nonce=" + authObj.getOauth_nonce()  + "" + authObj.getOauth_signature() + authObj.getOauth_signature_method() + authObj.getOauth_timestamp() + authObj.getOauth_version() + authObj.getOauth_callback();
//        String auth = "OAuth oauth_consumer_key=\"" + authObj.getOauth_consumer_key() + "\", " +
//                "oauth_nonce=\"" + authObj.getOauth_nonce() + "\", " +
//                "oauth_signature=\"" + authObj.getOauth_signature() + "\", " +
//                "oauth_signature_method=\"PLAINTEXT\", " +
//                "oauth_timestamp=\"" + authObj.getOauth_timestamp() + "\", oauth_callback=\"" + authObj.getOauth_callback() + "\"";
//        log.info("\n\n\nOauth to string {}  \n {}\n {} \n {} \n", new OAuth().toString(), authString, auth, test);
        HttpRequest resultHttp = HttpHelper.generateRequest(DiscogsUrls.DISCOGS_BASE_URL + DiscogsUrls.AUTH_PATH, OAuth.generateOAuthHeader());
        HttpResponse response = HttpHelper.sendRequest(resultHttp);
        log.info("\n\n\nResultHttp {}\n\n\n", resultHttp);
        log.info("\n\n\nresponse from HttpHelper {}\n\n\n", response);

//        log.info("result {}", result);
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
