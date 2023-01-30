package com.recordcataloguer.recordcataloguer.helpers.regex;

import com.recordcataloguer.recordcataloguer.dto.discogs.VisionFilteredText;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static List<String> getCatalogNumberAndTitlesMap(String visionText) {
        // Take vision text
        // get text for individual albums
        // List<String> albumsTexts
        // Search regex for each
        List<String> individualTextForAlbums = getTextForIndividualAlbums(visionText);

        List<String> maybeCatAndTitle = searchForCatalogNumberWithDashOrSpace(individualTextForAlbums);

        return maybeCatAndTitle;
    }

    /***
     * This method attempts to get a key value pair of catNo -> title by:
     * 1. searching for catNo with regex "abc-123"
     * 2. If found, the match is the key and the value is the original text with the catNo removed
     * 3. Then searches for number of whitespaces in each, to increase likelihood of accuracy
     * @param textsFromAlbums
     * @return
     */
    public static List<String> searchForCatalogNumberWithDashOrSpace(List<String> textsFromAlbums) {

        Pattern REGEX_ALPHANUMERIC = Pattern.compile(CATALOG_NUMBER_REGEX);
        List<String> catalogNumbers = new ArrayList<>();

        for (String text: textsFromAlbums) {
            VisionFilteredText vft = new VisionFilteredText();
            Matcher matchedText = REGEX_ALPHANUMERIC.matcher(text);
            List<String> matches = new ArrayList<>();
            vft.setTitle(text);
            while(matchedText.find()){
                matches.add(matchedText.group());
            }
            if(matches.size() == 0) {

            }
            // If only 1 match it's probably the catNo, so we can assume the rest of the text is the title??
//            String maybeCatNo = "";
//            String maybeTitle = "";
            // maybeCatNo StringUtils.isNumericSpace (123 12323)
            // If 1 match that contains a dash, then that is the only thing we have got to go on, but it should be the catalog number
            if(matches.size() == 1 && StringUtils.contains(matches.get(0), "-")) {
                catalogNumbers.addAll(matches);
                vft.setCatNo(matches.get(0));
//                maybeCatNo = matches.get(0);
//                maybeTitle = StringUtils.remove(text, matches.get(0));
//                maybeCatNoAndTitle.put(maybeCatNo, maybeTitle);
            }
            if(matches.size() == 1 && !StringUtils.contains(matches.get(0), "-")) {
                vft.setCatNo(matches.get(0));
                vft.setTitle(text);
                /**LIST Impl**/
//                catalogNumbers.addAll(matches);

                /**MAP IMPL**/
//                maybeCatNo = matches.get(0);
//                maybeTitle = StringUtils.remove(text, matches.get(0));
//                maybeCatNoAndTitle.put(maybeCatNo, maybeTitle);
            }
            // TODO: Implement
            if(matches.size() > 1){

            }

            getConfidenceLevel(maybeCatNo, maybeTitle);
        }


        return catalogNumbers;
    }

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
