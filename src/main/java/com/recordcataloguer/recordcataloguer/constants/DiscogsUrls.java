package com.recordcataloguer.recordcataloguer.constants;

public class DiscogsUrls {

    /**BASE URLs**/
    public static final String DISCOGS_API_BASE_URL = "https://api.discogs.com";
    private static final String DISCOGS_BASE_URL = "https://discogs.com";

    /**APIs**/
    public static final String DATABASE_API = "/database";
    public static final String USER_COLLECTION_API = "/users";
    public static final String OAUTH_API = "/oauth";
    public static final String MARKETPLACE_API = "/marketplace";

    /**ENDPOINTS**/
    public static final String REQUEST_TOKEN_ENDPOINT = "/request_token";
    public static final String ACCESS_TOKEN_ENDPOINT = "/access_token";
    public static final String IDENTITY_ENDPOINT = "/identity";
    public static final String ADD_TO_COLLECTION_ENDPOINT = "/collection/folders/";
    public static final String PRICE_SUGGESTIONS_ENDPOINT = "/price_suggestions";
    public static final String SEARCH_ENDPOINT = "/search";

    /**FULL URLs**/
    public static final String REQUEST_TOKEN_URL = DISCOGS_API_BASE_URL + OAUTH_API + REQUEST_TOKEN_ENDPOINT;
    public static final String ACCESS_TOKEN_URL = DISCOGS_API_BASE_URL + OAUTH_API + ACCESS_TOKEN_ENDPOINT;
    public static final String AUTHORIZATION_URL = DISCOGS_BASE_URL + OAUTH_API + "/authorize";
    public static final String IDENTITY_URL = DISCOGS_API_BASE_URL + OAUTH_API + IDENTITY_ENDPOINT;
    public static final String DATABASE_SEARCH_URL = DISCOGS_API_BASE_URL + DATABASE_API + SEARCH_ENDPOINT;

}
