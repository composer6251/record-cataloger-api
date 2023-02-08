package com.recordcataloguer.recordcataloguer.helpers.regex;

import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.recordcataloguer.recordcataloguer.helpers.regex.VisionTextFiltering.*;

@Slf4j
public class ImageReaderRegex {
    public static String regexOne = "[a-zA-Z][-/s][0-9?]";

    public static String releaseNumberRegex = "\\d{3,}$";
    String regexTwo = "[a-zA-Z]+[\\s|\\-][\\d]+";
    String dataYouWant = StringUtils.substringBetween("/Albert-King-New-Orleans-Heat/release/9282862", "release/");

    // TODO: Implement
    private static Map<String, String> searchForCatalogNumberWithOnlyDigitsAndSpace() {
        Map<String, String> maybeCatalogNumberAndTitle = new HashMap<>();

        return maybeCatalogNumberAndTitle;
    }

}
