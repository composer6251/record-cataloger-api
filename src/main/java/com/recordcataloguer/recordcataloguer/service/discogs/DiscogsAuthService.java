package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsAuthClient;
import com.recordcataloguer.recordcataloguer.helpers.discogs.auth.DiscogsAuthHelper;
import com.recordcataloguer.recordcataloguer.helpers.httpclienthelper.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.Map;

@Service
@Slf4j
public class DiscogsAuthService {

    @Autowired
    private DiscogsAuthClient discogsAuthClient;

    private String oauthToken;

    public String getAuthorizationUrlFeign() {
        log.info("received request to retrieve user authorization URL");
        // Call Request Token Endpoint to get OAuth Token
        Map<String, String> oAuthTokenMap = getRequestToken();
        // Generate authURL for user to verify
         String authUrl = oAuthTokenMap == null ? "" : HttpUtil.generateUserAuthorizationUrl(oAuthTokenMap.get("oauth_token"));
        // Front end user verifies
        // Then use key_verifier to request access token
        // Get oauth_token value
        String accessTokenRequestAuthorization = DiscogsAuthHelper.generateOAuthHeaderForAccessTokenRequest(oAuthTokenMap.get("oauth_token"), oAuthTokenMap.get("oauth_token_secret"));

        return discogsAuthClient.requestAccessToken(accessTokenRequestAuthorization, "record-cataloguer/1.0");
    }

    public String verifyIdentity() {
        log.info("received request to verify user identity");
        // Get Token
        String authorizationHeader = DiscogsAuthHelper.generateAuthorizationForUserActions(DiscogsTokens.DISCOGS_OAUTH_TOKEN, DiscogsTokens.DISCOGS_OAUTH_TOKEN_SECRET);
        HttpResponse response = discogsAuthClient.verifyUserIdentity(authorizationHeader, "record-cataloger/1.0");

        return "";
    }

    private Map<String, String> getRequestToken() {
        // Get OAuth TOken response
        String response = discogsAuthClient.requestOAuthToken(DiscogsAuthHelper.generateOAuthHeaderForOAuthTokenRequest(), "record-cataloger/1.0");
        // create map of response for k-v pair
        Map<String, String> oAuthTokenResponse = HttpUtil.getOAuthInfoFromDiscogsAuthResponse(response);

        return oAuthTokenResponse;
    }

    private Map<String, String> getOauthToken() {
        // Get OAuth TOken response
        String response = discogsAuthClient.requestOAuthToken(DiscogsAuthHelper.generateOAuthHeaderForOAuthTokenRequest(), "record-cataloger/1.0");
        // create map of response for k-v pair
        Map<String, String> oAuthTokenResponse = HttpUtil.getOAuthInfoFromDiscogsAuthResponse(response);

        return oAuthTokenResponse;
    }

    public String getAccessTokenFeign() {
        Map<String, String> oAuthTokenMap = getOauthToken();
        String auth = DiscogsAuthHelper.generateOAuthHeaderForAccessTokenRequest(oAuthTokenMap.get("oauth_token"), oAuthTokenMap.get("oauth_token_secret"));
        String accessTokenResponse = discogsAuthClient.requestAccessToken(auth, "record-cataloguer/1.0");

        return accessTokenResponse;
    }

}
