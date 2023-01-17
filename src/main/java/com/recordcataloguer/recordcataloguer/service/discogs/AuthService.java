package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsAuthClient;
import com.recordcataloguer.recordcataloguer.helpers.auth.AuthHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private DiscogsAuthClient discogsAuthClient;

    public String getAuthorizationUrlFeign() {
        log.info("received request to retrieve user authorization URL");
        // Get Token
        String response = discogsAuthClient.requestToken(AuthHelper.getOAuthHeaderMap(), "record-cataloger/1.0");
        Optional<String> url = AuthHelper.getOAuthToken();
        return url.orElse("");
    }

}
