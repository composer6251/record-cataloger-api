package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.database.hibernate.HibernateUtil;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.helpers.string.StringHelper;
import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.PriceSuggestionResponse;
import com.recordcataloguer.recordcataloguer.helpers.auth.AuthHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
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

    public String uploadRelease() {
        log.info("received request to retrieve user authorization URL");

        String authHeader = AuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        discogsClient.uploadAlbumReleaseToUncategorizedCollection(authHeader, "dfennell31@gmail.com", "1", "placeholder");
        return authHeader;
    }

    public PriceSuggestionResponse getPriceSuggestions(int releaseId) {
        log.info("received request to getPriceSuggestions");

        String authHeader = AuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        PriceSuggestionResponse priceSuggestionsResponse = discogsClient.getPriceSuggestions(authHeader, releaseId);

        return priceSuggestionsResponse;
    }

    public String verifyIdentity() {
        log.info("received request to verify user identity");

        Optional<String> url = AuthHelper.getOAuthToken();
        return url.orElse("");
    }

    public List<Album> getRecords(String imageUrl) {
        log.info("received request to getRecords by imageUrl");

        List<Album> discogsSearchResponse = getRecordsByImageText(imageUrl);

        return discogsSearchResponse;
    }

    public String getAuthorizationUrl() {
        log.info("received request to retrieve user authorization URL");

        Optional<String> url = AuthHelper.getOAuthToken();
        return url.orElse("");
    }

    /***
     * DiscogsService controller method for getting records by an image
     * @param imageUrl
     * @return ResponseEntity of DiscogsResponse
     */
    private List<Album> getRecordsByImageText(String imageUrl) {

        List<String> catalogueNumbers = new ArrayList<>();
//        List<String> catNoMap = new HashMap<>();
        boolean testingNewWay = true;
        if(testingNewWay) {
            catalogueNumbers = extractCatalogueNumbersFromImageAsMap(imageUrl);
        }
        else {
            catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);

        }

        if(catalogueNumbers.isEmpty()) return null;

        List<Album> albums = getRecordsByCatalogueNumber(catalogueNumbers);

        return albums;
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

    private List<String> extractCatalogueNumbersFromImageAsMap(String url){

        List<String> extractedCatalogueNumbers = imageReader.extractCatalogueNumberFromImageAsMap(url);

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
    private List<Album> getRecordsByCatalogueNumber(List<String> catalogueNumbers) {

        List<Album> albums = new ArrayList<>();

        for (String catalogueNumber : catalogueNumbers) {

            DiscogsSearchResponse discogsSearchResponse = discogsClient.getDiscogsRecordByCategoryNumber(catalogueNumber, token, country, format);
            if(discogsSearchResponse.getAlbums().isEmpty()) continue;
            for (Album album : discogsSearchResponse.getAlbums()) {
                album.setCatalogNumberForLookup(catalogueNumber);
            }
            albums.addAll(discogsSearchResponse.getAlbums());
        }

        List<Album> allFilteredAlbums = albums
                .stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Album::getTitle))
                .collect(Collectors.toList());

        List<Album> resultsWithPriceSuggestions = new ArrayList<>();
        for (Album album : allFilteredAlbums) {
            if(!Objects.nonNull(album)) continue;

            PriceSuggestionResponse priceSuggestionResponse = getPriceSuggestions(album.getId());

            try {
                if(priceSuggestionResponse.getGood() != null) album.setAlbumGoodValue(priceSuggestionResponse.getGood().getValue());
                if(priceSuggestionResponse.getMint() != null) album.setAlbumMintPlusValue(priceSuggestionResponse.getMint().getValue());
            }
            catch (NullPointerException exception) {
                log.info("Exception assiging album value to release {} and catno {} with message\n {}", album.getId(), album.getCatno(), exception.getMessage());
            }

            resultsWithPriceSuggestions.add(album);
        }

        return resultsWithPriceSuggestions;
    }

    public void insertRecordIntoDB(AlbumEntity album, Session session) {


    }

    @SneakyThrows
    public List<AlbumEntity> getAllDiscogsCatalogNumbers() {

        // Get a list of all discogs catalogue numbers

        // Maybe see if there's are patterns in order of text on album spine

        // Create multiple regexs
        // "abc-123" if found move on, else do 2 then 3
        // "abc 123"
        // "Captal records 9000
        // Maybe extract label and use it? Need list of labels?
        SessionFactory sessionFactory = HibernateUtil.getSessionFactoryXml();
        Session session = sessionFactory.openSession();
        DiscogsSearchResponse response = discogsClient.getAllDiscogsCatalogNumbers(token, format, 100);
        List<AlbumEntity> resultsToReturn = new ArrayList<>();
        AtomicReference<Long> failedTransactions = new AtomicReference<>(0L);

        int i = 1;
        while(response.getPagination() != null && response.getPagination().getUrls().containsKey("next") && i < 101) {
            if(i % 20 == 0){
                log.info("Pausing requests for 1 minute. result size {}", resultsToReturn.size());
                TimeUnit.MINUTES.sleep(1);
            }
            DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(token, format, 100, i++);

            List<AlbumEntity> allFilteredAlbums = resp.getAlbums()
                    .stream()
                    .filter(r -> !Objects.equals(r.getCatno(), ""))
                    .map(album -> AlbumEntity.buildAlbumEntityFromAlbum(album))
                    .collect(Collectors.toList());

//            session.beginTransaction();
//
//            resultsToReturn.forEach(r -> {
//                try {
//                    session.persist(r);
//
//                } catch (Exception exception) {
//                    log.error("Error inserting record {} {} \n {}", r.getCatno(), r.getTitle(), exception.getMessage());
//                    failedTransactions.getAndSet(failedTransactions.get() + 1);
//
//                }
//            });
//            session.getTransaction().commit();
////            session.flush();
            resultsToReturn.addAll(allFilteredAlbums);
            log.info("resultsToReturn size {}", resultsToReturn.size());

        }
        log.info("Number of failed Record inserts {} and total results {}", failedTransactions, resultsToReturn);
        session.close();

        return resultsToReturn;
    }

    public List<Album> getNextPageOfResults(DiscogsSearchResponse response) {
        List<Album> allFilteredAlbums = new ArrayList<>();
            int nextPageNumber = Integer.parseInt(StringHelper.getSubstringParam(response.getPagination().getUrls().get("next"), "&page=", "EnD"));
            DiscogsSearchResponse resp = discogsClient.getNextDiscogsSearchResultPage(token, format, 100, nextPageNumber);

            allFilteredAlbums = resp.getAlbums()
                    .stream()
                    .filter(r -> !Objects.equals(r.getCatno(), ""))
                    .map(result -> Album.builder().catno(result.getCatno()).country(result.getCountry()).build())
                    .collect(Collectors.toList());

        return allFilteredAlbums;
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
}
