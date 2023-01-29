package com.recordcataloguer.recordcataloguer.helpers.auth;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.helpers.http.HttpHelper;
import com.recordcataloguer.recordcataloguer.helpers.http.HttpUtil;
import com.recordcataloguer.recordcataloguer.dto.discogs.OAuth;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AuthHelper {

    public static final String generateOAuthHeaderForOAuthTokenRequest() {



        OAuth oAuth = new OAuth();

        /***Content-Type: application/x-www-form-urlencoded
         Authorization:
             OAuth oauth_consumer_key="your_consumer_key",
             oauth_nonce="random_string_or_timestamp",
             oauth_signature="your_consumer_secret&",
             oauth_signature_method="PLAINTEXT",
             oauth_timestamp="current_timestamp",
             oauth_callback="your_callback"
         User-Agent: some_user_agent**/

        String auth = "OAuth oauth_consumer_key=\"" + oAuth.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuth.getOauth_nonce() + "\", " +
                "oauth_signature=\"" + oAuth.getOauth_signature() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuth.getOauth_timestamp() + "\"";// +
//                ", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        return auth;
    }

    public static final String generateOAuthHeaderForAccessTokenRequest(String oAuthToken, String oAuthTokenSecret) {


        /***
         * Content-Type: application/x-www-form-urlencoded
         * Authorization:
         *         OAuth oauth_consumer_key="your_consumer_key",
         *         oauth_nonce="random_string_or_timestamp",
         *         oauth_token="oauth_token_received_from_step_2"
         *         oauth_signature="your_consumer_secret&",
         *         oauth_signature_method="PLAINTEXT",
         *         oauth_timestamp="current_timestamp",
         *         oauth_verifier="users_verifier"
         * User-Agent: some_user_agent
         */


        OAuth oAuth = new OAuth();

        String auth =
                "OAuth oauth_consumer_key=\"" + oAuth.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuth.getOauth_nonce() + "\", " +
                "oauth_token=\"" + oAuthToken + "\", " +
                "oauth_signature=\"" + oAuth.getOauth_signature() + oAuthTokenSecret + "\", " +
                "oauth_verifier=\"" + oAuth.getOauth_verifier() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuth.getOauth_timestamp() + "\"";// +
//                ", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        return auth;
    }

    public static String generateAuthorizationForUserActions(String oAuthToken, String oAuthTokenSecret) {
        OAuth oAuth = new OAuth();

        String auth =
                "OAuth oauth_consumer_key=\"" + oAuth.getOauth_consumer_key() + "\", " +
                        "oauth_nonce=\"" + oAuth.getOauth_nonce() + "\", " +
                        "oauth_token=\"" + oAuthToken + "\", " +
                        "oauth_signature=\"" + oAuth.getOauth_signature() +  oAuthTokenSecret + "\", " +
                        "oauth_signature_method=\"PLAINTEXT\", " +
                        "oauth_timestamp=\"" + oAuth.getOauth_timestamp() + "\"";// +
        log.info("Generated Auth Header for User Actions \n{}", auth);
        return auth;

    }

    // TODO: This won't work with feign. Each map entry is seen as it's own header and authorization is a single header.
    public static Map<String, String> getOAuthHeaderMap(){
        OAuth oAuth = new OAuth();

        Map<String, String> authorizationHeader = new HashMap<>();
        authorizationHeader.put("OAuth oauth_consumer_key=", oAuth.getOauth_consumer_key());
        authorizationHeader.put("oauth_nonce=", oAuth.getOauth_consumer_key());
        authorizationHeader.put("oauth_signature=", oAuth.getOauth_consumer_key());
        authorizationHeader.put("oauth_signature_method=", oAuth.getOauth_consumer_key());
        authorizationHeader.put("oauth_timestamp=", oAuth.getOauth_consumer_key());

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
