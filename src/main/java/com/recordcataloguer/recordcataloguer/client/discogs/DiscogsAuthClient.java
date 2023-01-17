package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.config.FeignConfiguration;
import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "authService", url = DiscogsUrls.DISCOGS_API_BASE_URL)
public interface DiscogsAuthClient {

    //TODO: TRY TO MIMIC POSTMAN REQUEST

    @GetMapping(value = DiscogsUrls.OAUTH_API + DiscogsUrls.REQUEST_TOKEN_URL, consumes = "application/x-www-form-urlencoded")
    String requestToken(
            @RequestHeader("Authorization") Map<String, String> auth,
            @RequestHeader("User-Agent") String userAgent
    );
}
