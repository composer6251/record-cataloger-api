package com.recordcataloguer.recordcataloguer.helpers.regex;

import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ImageReaderRegex {
    public static String regexOne = "[a-zA-Z][-/s][0-9?]";

    /***
     * Patterns:
     * abc-123456
     * abc123456
     * abc 123456
     * 123456
     * 80032-1
     */
    // Look for "-"
        // Grab chars on both sides until whitespace
    // Look for only digits
            // Then check for alphanumeric chars preceeding it up until whitespace

    // Possibly return String and let user choose?
    // Or just return the albums?

    private static final List<String> COMMON_TITLE_WORDS =  Arrays.asList("The", "Band");
    private static final String CATALOG_NUMBER_REGEX = "\\b[A-Za-z0-9_]+[\\s|\\-][\\d]+";
    private static final String ALPHANUMERIC_CAT_NO = "\\b[A-Za-z0-9_][\\d]+";

    public static String releaseNumberRegex = "\\d{3,}$";
    String regexTwo = "[a-zA-Z]+[\\s|\\-][\\d]+";
    String dataYouWant = StringUtils.substringBetween("/Albert-King-New-Orleans-Heat/release/9282862", "release/");

    public static List<String> extractRecordCatalogueNumber(String text) {

        // Pass in regex and individual text for albums to method
        // result passes into get confidence level
        // Retry with different regex if needed

        List<String> individualTextForAlbums = getTextForIndividualAlbums(text);
        Map<String, String> albumInfo = new HashMap<>();

        Pattern REGEX_ALPHANUMERIC = Pattern.compile(CATALOG_NUMBER_REGEX);
        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(text);
        List<String> matches = new ArrayList<>();
        while(matchedText.find()){
            matches.add(matchedText.group());
        }

        return matches;
    }

    public static List<DiscogsSearchAlbumRequest> getCatalogNumberAndTitlesMap(String visionText) {
        // Take vision text
        // get text for individual albums
        // List<String> albumsTexts
        // Search regex for each
        List<String> individualTextForAlbums = getTextForIndividualAlbums(visionText);

        List<DiscogsSearchAlbumRequest> albumSearchRequests = filterExtractedTextToBuildRequest(individualTextForAlbums);

        return albumSearchRequests;
    }

    /***
     * This method attempts to get a key value pair of catNo -> title by:
     * 1. searching for catNo with regex "abc-123"
     * 2. If found, the match is the key and the value is the original text with the catNo removed
     * 3. Then searches for number of whitespaces in each, to increase likelihood of accuracy
     * @param textsFromAlbums
     * @return
     */
    public static List<DiscogsSearchAlbumRequest> filterExtractedTextToBuildRequest(List<String> textsFromAlbums) {

        Pattern CAT_NO_WITH_DASH_OR_SPACE = Pattern.compile(CATALOG_NUMBER_REGEX);
        Pattern CAT_NO_WITHOUT_SPACE = Pattern.compile(ALPHANUMERIC_CAT_NO);

        List<DiscogsSearchAlbumRequest> albumsToSearch = new ArrayList<>();
        for (String text: textsFromAlbums) {
            DiscogsSearchAlbumRequest albumRequest = new DiscogsSearchAlbumRequest();
            Matcher matchedText = CAT_NO_WITH_DASH_OR_SPACE.matcher(text);
            List<String> matches = new ArrayList<>();
            albumRequest.setTitle(text);
            albumRequest.setOriginalString(text);

            while(matchedText.find()){
                matches.add(matchedText.group());
            }
            if(matches.size() == 0) {
                Matcher noDashOrSpace = CAT_NO_WITHOUT_SPACE.matcher(text);

                while(noDashOrSpace.find()){
                    matches.add(noDashOrSpace.group());
                }
            }
            // maybeCatNo StringUtils.isNumericSpace (123 12323)
            // If 1 match that contains a dash, then that is the only thing we have got to go on, but it should be the catalog number
            if(matches.size() == 1 && StringUtils.contains(matches.get(0), "-")) {
                albumRequest.setCatNo(matches.get(0));
            }
            if(matches.size() == 1 && !StringUtils.contains(matches.get(0), "-")) {
                albumRequest.setCatNo(matches.get(0));
                albumRequest.setTitle(text);
            }
            // TODO: Implement
            if(matches.size() > 1){

            }

            albumRequest.setConfidenceLevel(getConfidenceLevel(albumRequest.getCatNo(), albumRequest.getTitle()));

            // TODO: Spread albums further apart for pics
            // TODO: If text contains "Records"....can I backtrack and get chars only until eol or digit? Malago Records Z111
            // TODO: MAYBE RETURNING DERIVED VALUES FOR REQUEST AND LETTING USER DECIDE WHICH FIELDS TO USE IS THE BEST SOLUTION
            // TODO: OR MAKE SEPARATE CALLS IF CATNO RETURNS TOO MANY/TOO FEW RESULTS
            // TODO: OR TRY TO GET CATNO, ARTIST, ALBUM.....MAKE 2 CALLS WITH DIFF PARAMS AND FILTER DUPES THEN MATCH RESULTS ACCORDING TO PARAMS
            // TODO: MAYBE SEND ON REQUEST OF CATALOG NUMBERS ONLY. THEN COMPARE WITH ORIGINAL TEXT/OR ALBUM SEARCH RESULTS TO SEE IF RESULTS.ARTIST/ALBUM/CATNO/TITLE


            // COMPARE CATNO FROM RESULTS TO ORIGINAL?

            // If there's no catNo and the title is an unrealistic length for an album OR the catno is < 5 characters, it is probably not accurate
            if((StringUtils.isBlank(albumRequest.getCatNo()) && albumRequest.getOriginalString().length() < 10) || (!StringUtils.isBlank(albumRequest.getCatNo()) && albumRequest.getCatNo().length() < 4)) continue;
            albumsToSearch.add(albumRequest);

        }

        return albumsToSearch;
    }


    /** TEST DATA VISION TEXTS*
     * Actual values:
     *
     * 9
     * PE 33192
     * CHR 1235
     * 2MS 2024
     * 25386
     * KE 33290
     * Z. Z. HILL
     * TIPS 9349
     * ** AA-1099
     * 5120922
     * 0209
     * UA-LA974-H
     * AROMATO
     * MCA-5132
     * 7599-75386-1
     * AHLI-4247
     * POCO CANTAMOS
     * ECMASTER
     * POCO
     * ATI ANTIC 7 80148-1
     * MCA-5288
     * RY COODER BLUE CITY ORIGINAL MOTION PICTURE SOUNDTRACK
     * MICHAEL MURPHEY BLUE SKY NIGHT THUNDER
     * poco
     * THE MOTHERS OF INVENTION UNCLE MEAT / 2024
     * ●
     * LEGEND
     * DELIVERIN'
     * THE DIRT BAND
     * COZZHEAD OVER HEELS.
     * POCO ROSE OF CMARRON
     * POCO
     * UNDER THE GUN
     * ●
     * BLACK ON-BLACK WAYLON
     * RORY GALLAGHER/TOP PRIORITY
     * THE JESS RODEN BAND
     * POCO
     * KEEP YOUR HAT ON
     * AN AMERICAN DREAM
     * YMAMODATA
     * POCO COWBOYS AND ENGLISHMEN
     * WARNER BROS. RECORDS INC. PRINTED IN USA
     * CHRYSALIS RECORDS PRINTED IN U.S.A.
     * BIZARRE
     * UNITED ARTISTS
     * EPIC STEREO
     * 197A IN AND RECORD
     * EPIC STEREO X598
     * EPIC STERE
     * STEREO
     * **/


    private static List<String> getTextForIndividualAlbums(String visionResponse) {
        String[] textForIndividualAlbums = visionResponse.split("\n");

        return Arrays.stream(textForIndividualAlbums).toList();
    }

    // TODO: Implement
    private static Map<String, String> searchForCatalogNumberWithOnlyDigitsAndSpace() {
        Map<String, String> maybeCatalogNumberAndTitle = new HashMap<>();

        return maybeCatalogNumberAndTitle;
    }

    private static int getConfidenceLevel(String maybeCatNo, String maybeTitle) {
        /**values to determine accuracy of search**/
        boolean maybeTitleHasMoreWhiteSpaces = StringUtils.countMatches(maybeTitle, StringUtils.SPACE) > StringUtils.countMatches(maybeCatNo, StringUtils.SPACE);
        boolean maybeTitleIsAlphaOnly = StringUtils.isAlpha(StringUtils.remove(maybeTitle, " "));
        boolean maybeTitleContainsCommonWord = COMMON_TITLE_WORDS.contains(StringUtils.split(maybeTitle));
        boolean maybeCatNoContainsDash = StringUtils.contains(maybeCatNo, "-");

        int confidenceLevel = 0;
        if(maybeTitleHasMoreWhiteSpaces) confidenceLevel += 40;
        if(maybeTitleIsAlphaOnly) confidenceLevel += 30;
        if(maybeTitleContainsCommonWord) confidenceLevel += 20;
        if(maybeCatNoContainsDash) confidenceLevel += 40;

        return confidenceLevel;
    }

}
