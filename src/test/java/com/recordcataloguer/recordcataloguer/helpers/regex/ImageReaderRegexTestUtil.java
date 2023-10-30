package com.recordcataloguer.recordcataloguer.helpers.regex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageReaderRegexTestUtil {

    private static Map<String, String> testVisionResponseTexts = new HashMap<>();

    public static Map<String, String> buildVisionResponseTexts() {
        testVisionResponseTexts.put("AR-410008","My Text to test AR-410008");
        testVisionResponseTexts.put("AR-410008","SQ-17238\n" +
                "VR-024 ROOMFUL OF BLUES LIVE AT LUPO'S HEARTBREAK\n" +
                "THE BLUESBUSTERS\n" +
                "THIS TIME\n" +
                "9000 THE DYNATONES TOUGH TO SHAKE\n" +
                "Q\n" +
                "KGPS\n" +
                "25672-1\n" +
                "AL 3902\n" +
                "Sus\n" +
                "1\n" +
                "BRIZI IS\n" +
                "THE DYNATONES\n" +
                "STEREO\n" +
                "DELBE\n" +
                "THE LIVE ADVENTURES OF MIKE BLOOMFIELD AND AL H");

        return testVisionResponseTexts;
    }

    public static Map<String, String> buildVisionResponseTextsFull() {
        testVisionResponseTexts.put("AR-410008","My Text to test AR-410008");
        testVisionResponseTexts.put("AR-410008","SQ-17238\n" +
                "VR-024 ROOMFUL OF BLUES LIVE AT LUPO'S HEARTBREAK\n" +
                "THE BLUESBUSTERS\n" +
                "THIS TIME\n" +
                "9000 THE DYNATONES TOUGH TO SHAKE\n" +
                "Q\n" +
                "KGPS\n" +
                "25672-1\n" +
                "AL 3902\n" +
                "Sus\n" +
                "1\n" +
                "BRIZI IS\n" +
                "THE DYNATONES\n" +
                "STEREO\n" +
                "DELBE\n" +
                "THE LIVE ADVENTURES OF MIKE BLOOMFIELD AND AL H");

        return testVisionResponseTexts;
    }
}
