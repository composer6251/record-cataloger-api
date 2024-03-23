package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsAuthClient;
import com.recordcataloguer.recordcataloguer.dto.discogs.request.OAuthRequest;
import com.recordcataloguer.recordcataloguer.util.discogs.auth.DiscogsAuthHelper;
import com.recordcataloguer.recordcataloguer.util.httpclienthelper.HttpHelper;
import com.recordcataloguer.recordcataloguer.util.httpclienthelper.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.recordcataloguer.recordcataloguer.util.httpclienthelper.HttpUtil.getOAuthInfoFromDiscogsAuthResponse;

@Service
@Slf4j
public class DiscogsAuthService {

    @Autowired
    private DiscogsAuthClient discogsAuthClient;

    private String oauthToken = "SetcftMTyWckKqqfpaEjXkuazBqPDSMbNnZJTgea";

    public String generateUserAuthentication() {
        log.info("received request to retrieve user authorization URL");
        // TODO: Add user table and auth table to store user authentication info.
        // TODO: Check user/auth table first for existing tokens

        String oAuthTokenSecret = "";

        if(oauthToken == null) {
            // Generate Auth Header for request token
            String authHeader = generateOAuthHeaderForRequestToken();
            // Call Request Token Endpoint
            Map<String, String> reqToken = stepOneRequestToken(authHeader);

            oauthToken = reqToken.get("oauth_token");
            oAuthTokenSecret = reqToken.get("oauth_token_secret");

            String authorizationUrl = DiscogsUrls.AUTHORIZATION_URL + "?oauth_token=" + oauthToken;

            return authorizationUrl;
        }
        else {
            // Generate Auth header for access token
            String oAuthHeader = generateOAuthHeaderForAccessTokenRequest(oauthToken, oAuthTokenSecret);

            String accessToken = stepTwoAccessToken(oAuthHeader);

            String authorizationUrl = getRequestTokenAndSecret(oauthToken);

            return authorizationUrl;
        }
    }

    public static String getRequestTokenAndSecret(String oauthToken) {
        // Generate/Send Request
        HttpRequest resultHttp = HttpHelper.generateRequest(DiscogsUrls.REQUEST_TOKEN_URL, oauthToken);
        HttpResponse<String> response;
        response = HttpHelper.sendRequest(resultHttp);

        // Extract OAuth Token from response and generate URL
        String authUrl = response.body() == null ? "" : HttpUtil.generateUserAuthorizationUrl(response.body());

        return authUrl;
    }

    public Map<String, String> stepOneRequestToken(String authHeader) {
        // Generate/Send Request
        String resp = discogsAuthClient.getRequestToken(authHeader, DiscogsTokens.USER_AGENT);
        // Extract OAuth Token from response and generate URL
        Map<String, String> requestTokenMap = getOAuthInfoFromDiscogsAuthResponse(resp);

        return requestTokenMap;
    }

    public String stepTwoAccessToken(String authHeader) {
        // Generate/Send Request
        String resp = discogsAuthClient.getAccessToken(authHeader, DiscogsTokens.USER_AGENT);
        // Extract OAuth Token from response and generate URL

        return resp;
    }

    public static String generateOAuthHeaderForRequestToken() {

        OAuthRequest oAuthRequest = new OAuthRequest();

        String userActionAuthHeader =
                "OAuth oauth_consumer_key=\"" + DiscogsTokens.DISCOGS_CONSUMER_KEY + "\", " +
                        "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\", " +
                        "oauth_signature=\"" + DiscogsTokens.DISCOGS_CONSUMER_SECRET + "&\", " +
                        "oauth_signature_method=\"PLAINTEXT\", " +
                        "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\", " +
                        "oauth_callback=\"" + "" + "\"";

        return userActionAuthHeader;
    }

    public static String generateOAuthHeaderForAccessTokenRequest(String oAuthToken, String oAuthTokenSecret) {

        OAuthRequest oAuthRequest = new OAuthRequest();

        String accessTokenAuthHeader =
                "OAuth oauth_consumer_key=\"" + DiscogsTokens.DISCOGS_CONSUMER_KEY + "\", " +
                        "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\", " +
                        "oauth_token=\"" + oAuthToken + "\", " +
                        "oauth_signature=\"" + oAuthRequest.getOauth_signature() + "\", " +
                        "oauth_signature_method=\"PLAINTEXT\", " +
                        "oauth_verifier=\"" + DiscogsTokens.MY_OAUTH_VERIFIER_TOKEN + "\", " +
                        "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\", " +
                        "oauth_callback=\"" + "" + "\"";

        return accessTokenAuthHeader;
    }

    public static String generateOAuthHeaderForIdentityRequest(String oAuthToken, String oAuthTokenSecret) {

        OAuthRequest oAuthRequest = new OAuthRequest();

        String accessTokenAuthHeader =
                "OAuth oauth_consumer_key=\"" + DiscogsTokens.DISCOGS_CONSUMER_KEY + "\"," +
                        "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\"," +
                        "oauth_token=\"" + DiscogsTokens.DISCOG_OAUTH_TOKEN_FOR_USER_ACTION + "\"," +
                        "oauth_signature=\"" + DiscogsTokens.DISCOGS_CONSUMER_SECRET + "&" + DiscogsTokens.DISCOG_OAUTH_TOKEN_SECRET_FOR_USER_ACTION + "\"," +
                        "oauth_signature_method=\"PLAINTEXT\"," +
                        "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\"," +
                        "oauth_verifier=\"" + DiscogsTokens.MY_OAUTH_VERIFIER_TOKEN;

        return accessTokenAuthHeader;
    }

    public static String generateOAuthHeaderForInventoryRequest(String oAuthToken, String oAuthTokenSecret) {

        OAuthRequest oAuthRequest = new OAuthRequest();

        String accessTokenAuthHeader =
                "OAuth oauth_consumer_key=\"" + DiscogsTokens.DISCOGS_CONSUMER_KEY + "\"," +
                        "oauth_nonce=\"" + oAuthRequest.getOauth_nonce() + "\"," +
                        "oauth_signature=\"" + oAuthRequest.getOauth_signature() + "\"," +
                        "oauth_signature_method=\"HMAC-SHA1\"," +
                        "oauth_timestamp=\"" + oAuthRequest.getOauth_timestamp() + "\"," +
                        "oauth_token=\"" + oAuthToken + "\"," +
                        "oauth_version=\"" + oAuthRequest.getOauth_version();

        return accessTokenAuthHeader;
    }

    private Map<String, String> redirectUserForAuthorization() {
        // Get request token to redirect user for their authorization
        String response = discogsAuthClient.getRequestToken(DiscogsAuthHelper.generateOAuthHeaderForOAuthTokenRequest(), "record-cataloger/1.0");
        // create map of response for k-v pair
        Map<String, String> oAuthTokenResponse = getOAuthInfoFromDiscogsAuthResponse(response);

        return oAuthTokenResponse;
    }

    public String verifyIdentity(String username) {
        log.info("received request to verify user identity");
        // Get Token
        String authorizationHeader = generateOAuthHeaderForIdentityRequest(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        String response = discogsAuthClient.verifyUserIdentity(authorizationHeader, "record-cataloger/1.0");

        return response;
    }

    private Map<String, String> getOauthToken() {
        // Get OAuth Token response
        String response = discogsAuthClient.getRequestToken(DiscogsAuthHelper.generateOAuthHeaderForOAuthTokenRequest(), "record-cataloger/1.0");
        // create map of response for k-v pair
        Map<String, String> oAuthTokenResponse = getOAuthInfoFromDiscogsAuthResponse(response);

        return oAuthTokenResponse;
    }

    public String getAccessTokenFeign() {
        Map<String, String> oAuthTokenMap = getOauthToken();
        String auth = DiscogsAuthHelper.generateOAuthHeaderForAccessTokenRequest(oAuthTokenMap.get("oauth_token"), oAuthTokenMap.get("oauth_token_secret"));
        String accessTokenResponse = discogsAuthClient.getAccessToken(auth, "record-cataloguer/1.0");

        return accessTokenResponse;
    }

    //    public String getUserAccessToken() {
//
//        // Generate Auth Header for request token
////        String authHeader = generateOAuthHeaderForRequestToken();
////        // Call Request Token Endpoint to get OAuth Token
////        Map<String, String> oAuthTokenMap = getOAuthInfoFromDiscogsAuthResponse(authHeader);
////        // Generate authURL for user redirect to authenticate/authorize app access to their profile.
////        String discogsAuthorizationPage = "https://discogs.com/oauth/authorize?oauth_token=" + oAuthTokenMap.get("oauth_token");
////
//        //  String authUrl = oAuthTokenMap == null ? "" : HttpUtil.generateUserAuthorizationUrl(oAuthTokenMap.get("oauth_token"));
//
//        // Then use key_verifier to request access token
//        // Get oauth_token value
//        //String accessTokenRequestAuthorization = DiscogsAuthHelper.generateOAuthHeaderForAccessTokenRequest(oAuthTokenMap.get("oauth_token"), oAuthTokenMap.get("oauth_token_secret"));
//
//        return discogsAuthClient.requestAccessToken(accessTokenRequestAuthorization, "record-cataloguer/1.0");
//    }


}
