package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<DiscogsSearchResponse>> getRecords(String imageUrl) {

        List<String> catalogueNumbers = extractCatalogueNumbersFromImage(imageUrl);

        List<DiscogsSearchResponse> discogsSearchResponse = getRecordsByCatalogueNumber(catalogueNumbers);

        return ResponseEntity.of(Optional.of(discogsSearchResponse));
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
    private List<DiscogsSearchResponse> getRecordsByCatalogueNumber(List<String> catalogueNumbers) {
        List<DiscogsSearchResponse> discogsSearchResponseList = new ArrayList<>();

        DiscogsSearchResponse discogsSearchResponse = discogsClient.getUSDiscogsRecordsByCategoryNumber(catalogueNumbers.get(0), token, country, format);
//        log.info("discog single response: {}", discogsSearchResponse.getResults().toString());
//        catalogueNumbers.stream().map(catalogueNumber -> discogsSearchResponseList
//                .add(discogsClient.getUSDiscogsRecordsByCategoryNumber(catalogueNumber, token, country, format)));

        List<DiscogsSearchResponse> test = new ArrayList<>();
        test.add(discogsSearchResponse);
        return test;
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
