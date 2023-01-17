package com.recordcataloguer.recordcataloguer.helpers.auth;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.helpers.http.HttpHelper;
import com.recordcataloguer.recordcataloguer.helpers.http.HttpUtil;
import com.recordcataloguer.recordcataloguer.http.discogs.OAuth;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Slf4j
public class AuthHelper {

    public static final String generateOAuthHeader() {



        OAuth oAuth = new OAuth();

        String auth = "OAuth oauth_consumer_key=\"" + oAuth.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuth.getOauth_nonce() + "\", " +
                "oauth_signature=\"" + oAuth.getOauth_signature() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuth.getOauth_timestamp() + "\"";// +
//                ", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        String authTest = "OAuth oauth_consumer_key=\"" + oAuth.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuth.getOauth_nonce() + "\", " +
                "oauth_signature=\"" + oAuth.getOauth_signature() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuth.getOauth_timestamp() + "\"";// +
//                ", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        return auth;
    }

    public static Optional<String> getOAuthToken() {

        // Generate/Send Request
        HttpRequest resultHttp = HttpHelper.generateRequest(DiscogsUrls.DISCOGS_API_BASE_URL + DiscogsUrls.REQUEST_TOKEN, generateOAuthHeader());
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
        HttpRequest accessTokenRequest = HttpHelper.generateRequest(DiscogsUrls.DISCOGS_API_BASE_URL + DiscogsUrls.OAUTH_PATH + DiscogsUrls.ACCESS_TOKEN, generateOAuthHeader());
        HttpResponse accessTokenResponse = HttpHelper.sendRequest(accessTokenRequest);
        log.info("Access Token Response {}", accessTokenResponse);
    }
}
