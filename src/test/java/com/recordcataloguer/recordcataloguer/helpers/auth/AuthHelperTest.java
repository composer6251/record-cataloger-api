package com.recordcataloguer.recordcataloguer.helpers.auth;

import com.recordcataloguer.recordcataloguer.http.discogs.OAuth;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AuthHelperTest {

    @Test
    public void generateAuthorizationHeaderShouldWork() {

        OAuth oAuth = new OAuth();

        Map<String, String> authorizationHeader = new HashMap<>();
        authorizationHeader.put("OAuth oauth_consumer_key=", oAuth.getOauth_consumer_key());
        System.out.println("authHeader " + authorizationHeader);
    }
}
