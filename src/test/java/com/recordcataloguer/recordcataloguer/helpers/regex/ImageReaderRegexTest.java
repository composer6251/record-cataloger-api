package com.recordcataloguer.recordcataloguer.helpers.regex;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageReaderRegexTest {

    @Test
    public void imageReaderRegexShouldFindNumber() {
        String regexAlphaSpaceExplained =
                "\b" + // Where a word starts or ends
                "[a-zA-Z]+" + // Any number of uppercase or lowercase letters
                "[\\s|-]" + // A space OR a hyphen
                "[\\d]+";   // Any number of digits
        String regexAlphaSpaceDigits = "\\b[a-zA-Z]+[\\s][\\d]+";
        String regexAlphaNumeric = "\\b[A-Za-z0-9_]+[\\s|\\-][\\d]+";
        String regexTwo = "[a-zA-Z]+[\\s|\\-][\\d]+";
        Pattern REGEX_ALPHANUMERIC = Pattern.compile(regexAlphaNumeric);
        // Find digits
        // Check before is either - or space
        // Get alphanumeric chars before - or space

        String testExtractedText = "My Text to test AR 410008 and 32";
        String testSpace = "AR 410008";
        String testHyphen = "AR-410008";
        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(testExtractedText);
        List<String> matches = new ArrayList<>();
        while(matchedText.find()){
            matches.add(matchedText.group());
        }
        System.out.println(matches);
        System.out.println(Pattern.matches(regexTwo, testHyphen));
        System.out.println(Pattern.matches(regexAlphaNumeric, testSpace));
    }
}
