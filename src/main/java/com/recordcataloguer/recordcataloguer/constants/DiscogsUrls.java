package com.recordcataloguer.recordcataloguer.constants;

public class DiscogsUrls {

    /**BASE URLs**/
    public static final String DISCOGS_API_BASE_URL = "https://api.discogs.com";
    private static final String DISCOGS_BASE_URL = "https://discogs.com";

    /**APIs**/
    public static final String DATABASE_API = "/database";
    public static final String OAUTH_API = "/oauth";

    /**ENDPOINTS**/
    public static final String REQUEST_TOKEN_ENDPOINT = "/request_token";
    public static final String ACCESS_TOKEN_ENDPOINT = "/access_token";

    public static final String SEARCH_ENDPOINT = "/search";


    // TODO: Create endpoint constants

    /**AUTH**/
    public static final String REQUEST_TOKEN_URL = DISCOGS_API_BASE_URL + OAUTH_API + REQUEST_TOKEN_ENDPOINT;
    public static final String ACCESS_TOKEN_URL = DISCOGS_API_BASE_URL + OAUTH_API + ACCESS_TOKEN_ENDPOINT;

    public static final String DATABASE_SEARCH_URL = DISCOGS_API_BASE_URL + DATABASE_API + SEARCH_ENDPOINT;
}
