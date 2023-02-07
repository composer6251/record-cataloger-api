package com.recordcataloguer.recordcataloguer.helpers.regex;

import java.util.Arrays;
import java.util.List;

public class VisionTextFiltering {

    /***CatNo Patterns*
     *     /***
     *      * abc-123456
     *      * abc123456
     *      * abc 123456
     *      * 123456
     *      * 80032-1
     *      */


    public static final List<String> COMMON_TITLE_WORDS =  Arrays.asList("The", "Band");
    public static final String CATALOG_NUMBER_REGEX = "\\b[A-Za-z0-9_]+[\\s|\\-][\\d]+";
    // 2-4 chars "-" or space 3-5 digits
    public static final String CAT_NO_MOST_COMMON = "\\b[A-Za-z]{2,4}[\\s|\\-][\\d]{3,5}";
    public static final String CAT_NO = "\\b[A-Za-z0-9_][\\d]+";
    public static final String CAT_NO_LENGTH_SIX_PLUS = "\\b[A-Za-z0-9_][\\d]+";
}
