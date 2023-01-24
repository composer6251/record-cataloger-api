package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.config.FeignConfiguration;
import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpResponse;
import java.util.Map;

@FeignClient(name = "authService", url = DiscogsUrls.DISCOGS_API_BASE_URL)
public interface DiscogsAuthClient {

    @GetMapping(value = DiscogsUrls.OAUTH_API + DiscogsUrls.REQUEST_TOKEN_ENDPOINT, consumes = "application/x-www-form-urlencoded")
    String requestOAuthToken(
            @RequestHeader("Authorization") String auth,
            @RequestHeader("User-Agent") String userAgent
    );

    @PostMapping(value = DiscogsUrls.OAUTH_API + DiscogsUrls.ACCESS_TOKEN_ENDPOINT, consumes = "application/x-www-form-urlencoded")
    String requestAccessToken(
            @RequestHeader("Authorization") String auth,
            @RequestHeader("User-Agent") String userAgent
    );

    @GetMapping(value = DiscogsUrls.OAUTH_API + DiscogsUrls.IDENTITY_ENDPOINT, consumes = "application/x-www-form-urlencoded")
    HttpResponse verifyUserIdentity(
            @RequestHeader("Authorization") String auth,
            @RequestHeader("User-Agent") String userAgent
    );
}
