package com.recordcataloguer.recordcataloguer.helpers.auth;

import com.recordcataloguer.recordcataloguer.dto.discogs.OAuthRequest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AuthHelperTest {

    @Test
    public void generateAuthorizationHeaderShouldWork() {

        OAuthRequest oAuthRequest = new OAuthRequest();

        Map<String, String> authorizationHeader = new HashMap<>();
        authorizationHeader.put("OAuth oauth_consumer_key=", oAuthRequest.getOauth_consumer_key());
        System.out.println("authHeader " + authorizationHeader);
    }
}
