//package com.recordcataloguer.recordcataloguer.helpers.regex;
//
//import com.recordcataloguer.recordcataloguer.helpers.image.ImageReader;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ImageReaderRegexTest {
//
//
//    @Test
//    public void imageReaderRegexShouldFindNumber() {
//
//        // TODO: Find a better way to do this
//        String data = "/Albert-King-New-Orleans-Heat/release/9282862";
//        StringBuilder dataWithEnd = new StringBuilder("/Albert-King-New-Orleans-Heat/release/9282862");
//        dataWithEnd = dataWithEnd.append("EnD");
//        String dataYouWant = StringUtils.substringBetween(dataWithEnd.toString(), "release/", "EnD");
//        String regexAlphaSpaceExplained =
//                "\b" + // Where a word starts or ends
//                "[a-zA-Z]+" + // Any number of uppercase or lowercase letters
//                "[\\s|-]" + // A space OR a hyphen
//                "[\\d]+";   // Any number of digits
//        String regexAlphaSpaceDigits = "\\b[a-zA-Z]+[\\s][\\d]+";
//        String regexAlphaNumeric = "\\b[A-Za-z0-9_]+[\\s|\\-][\\d]+";
//        String regexTwo = "[a-zA-Z]+[\\s|\\-][\\d]+";
//        Pattern REGEX_ALPHANUMERIC = Pattern.compile(regexAlphaNumeric);
//        // Find digits
//        // Check before is either - or space
//        // Get alphanumeric chars before - or space
//
//        String testExtractedText = "My Text to test AR 410008 and 32";
//        String testSpace = "AR 410008";
//        String testHyphen = "AR-410008";
//        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(testExtractedText);
//        List<String> matches = new ArrayList<>();
//        while(matchedText.find()){
//            matches.add(matchedText.group());
//        }
//        System.out.println(matches);
//        System.out.println(Pattern.matches(regexTwo, testHyphen));
//        System.out.println(Pattern.matches(regexAlphaNumeric, testSpace));
//    }
//
//
//
//    @Test
//    public void catalogNumberShouldBeExtractedFromText() {
//
//        Map<String, String> testTexts = ImageReaderRegexTestUtil.buildVisionResponseTexts();
//
//        ImageReaderRegex.searchForCatalogNumberWithDashOrSpace(testTexts.get("AR-410008"));
//    }
//
//    @Test
//    public void catalogNumberShouldBeExtractedImage() {
//
//        ImageReader imageReader = new ImageReader();
//        String text = imageReader.extractTextFromImage("/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/IMG_0214.jpeg");
//        ImageReaderRegex.searchForCatalogNumberWithDashOrSpace(text);
//        System.out.println();
//    }
//}
