package com.recordcataloguer.recordcataloguer.helpers.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageReaderRegex {
    public static String regexOne = "[a-zA-Z][-/s][0-9?]";
    public static List<String> extractRecordCatalogueNumber(String text) {

        // TODO: see if catNo have predictable number of prefixes from record label
        // TODO: filter out catNo with \n in them?????
        String regexAlphaNumeric = "\\b[A-Za-z0-9_]+[\\s|\\-][\\d]+";
        String regexTwo = "[a-zA-Z]+[\\s|\\-][\\d]+";
        Pattern REGEX_ALPHANUMERIC = Pattern.compile(regexAlphaNumeric);
        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(text);
        List<String> matches = new ArrayList<>();
        while(matchedText.find()){
            matches.add(matchedText.group());
        }

        // Look for pattern uncertain alphaNumeric characters, space or dash, uncertain alphaNumeric characters

        // Look for numbers, then take x numbers of characters before it and possibly after it?

        // Look for alphanumeric any # + " " or "-" + numeric any #

        String test = "AR 410008";

        System.out.println(Pattern.matches(regexOne, test));

        return matches;
    }

}
