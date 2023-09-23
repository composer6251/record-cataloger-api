package com.recordcataloguer.recordcataloguer.helpers.auth;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.helpers.httpclienthelper.HttpHelper;
import com.recordcataloguer.recordcataloguer.helpers.httpclienthelper.HttpUtil;
import com.recordcataloguer.recordcataloguer.dto.discogs.OAuthRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AuthHelper {

    private static String accessTokenAuthHeader;
    private static String userActionAuthHeader;

    public static final String generateOAuthHeaderForOAuthTokenRequest() {
        if(!userActionAuthHeader.isBlank()) return userActionAuthHeader;

        OAuthRequest oAuthRequest = new OAuthRequest();

        String auth = "OAuth oauth_consumer_key=\"" + oAuthRequest.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\", " +
                "oauth_signature=\"" + oAuthRequest.getOauth_signature() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\"";// +
//                ", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        userActionAuthHeader = auth;
        return userActionAuthHeader;
    }

    public static final String generateOAuthHeaderForAccessTokenRequest(String oAuthToken, String oAuthTokenSecret) {
        if(!accessTokenAuthHeader.isBlank()) return accessTokenAuthHeader;

        OAuthRequest oAuthRequest = new OAuthRequest();

        String auth =
                "OAuth oauth_consumer_key=\"" + oAuthRequest.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\", " +
                "oauth_token=\"" + oAuthToken + "\", " +
                "oauth_signature=\"" + oAuthRequest.getOauth_signature() + oAuthTokenSecret + "\", " +
                "oauth_verifier=\"" + oAuthRequest.getOauth_verifier() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\"";// +
//                ", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        accessTokenAuthHeader = auth;
        return accessTokenAuthHeader;
    }

    public static String generateAuthorizationForUserActions(String oAuthToken, String oAuthTokenSecret) {

        OAuthRequest oAuthRequest = new OAuthRequest();

        String auth =
                "OAuth oauth_consumer_key=\"" + oAuthRequest.getOauth_consumer_key() + "\", " +
                        "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\", " +
                        "oauth_token=\"" + oAuthToken + "\", " +
                        "oauth_signature=\"" + oAuthRequest.getOauth_signature() +  oAuthTokenSecret + "\", " +
                        "oauth_signature_method=\"PLAINTEXT\", " +
                        "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\"";// +
        return auth;

    }

    // TODO: This won't work with feign. Each map entry is seen as it's own header and authorization is a single header.
    public static Map<String, String> getOAuthHeaderMap(){
        OAuthRequest oAuthRequest = new OAuthRequest();

        Map<String, String> authorizationHeader = new HashMap<>();
        authorizationHeader.put("OAuth oauth_consumer_key=", oAuthRequest.getOauth_consumer_key());
        authorizationHeader.put("oauth_nonce=", oAuthRequest.getOauth_consumer_key());
        authorizationHeader.put("oauth_signature=", oAuthRequest.getOauth_consumer_key());
        authorizationHeader.put("oauth_signature_method=", oAuthRequest.getOauth_consumer_key());
        authorizationHeader.put("oauth_timestamp=", oAuthRequest.getOauth_consumer_key());

        log.info("authHeader", authorizationHeader);

        return authorizationHeader;
    }

    public static Optional<String> getOAuthToken() {

        // Generate/Send Request
        HttpRequest resultHttp = HttpHelper.generateRequest(DiscogsUrls.REQUEST_TOKEN_URL, generateOAuthHeaderForOAuthTokenRequest());
        // TODO: Raw usage???
        HttpResponse response;
        response = HttpHelper.sendRequest(resultHttp);
        log.info("\n\n\nresponse from HttpHelper {}\n\n\n", response);

        // Extract OAuth Token from response and generate URL
        String authUrl = response.body() == null ? "" : HttpUtil.generateUserAuthorizationUrl(response.body().toString());
        log.info("Returning Authorization Url {}", authUrl);

        return Optional.of(authUrl);
    }

    public static void getAccessToken() {
        log.info("received request to retrieve user access token");
        // Get Token
        HttpRequest accessTokenRequest = HttpHelper.generateRequest(DiscogsUrls.ACCESS_TOKEN_URL, generateOAuthHeaderForOAuthTokenRequest());
        HttpResponse accessTokenResponse = HttpHelper.sendRequest(accessTokenRequest);
        log.info("Access Token Response {}", accessTokenResponse);
    }
}
