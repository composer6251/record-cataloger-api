package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsNewTokens;
import com.recordcataloguer.recordcataloguer.config.FeignConfiguration;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.http.discogs.OAuth;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "discogsService", url = DiscogsUrls.DISCOGS_BASE_URL, configuration = FeignConfiguration.class)
public interface DiscogsClient {

    //TODO: TRY TO MIMIC POSTMAN REQUEST

//     OAuth oauth_consumer_key="jzTcIyulkcEoOTEZLSmU",oauth_token="EWVAPiForTZCsgRTwOrMJPdQqcGixHscVPaeagAb",
//     oauth_signature_method="PLAINTEXT",oauth_timestamp="1673550948",oauth_nonce="ce61f359-3403-482c-a31c-8e4df85a2301",
//     oauth_version="1.0",oauth_signature="rwYTcKOKqssuGwbwNHlpkMYpspJuYbEC%26fYDOZwegKbkpEcpidfVMUnaYdNuHPYPQpFoMBUFL"

    @GetMapping(value = DiscogsUrls.DATABASE_API + DiscogsUrls.SEARCH_PATH, consumes = "application/json")
    DiscogsSearchResponse getDiscogsRecordByCategoryNumber(
            @RequestParam("catno") String catalogueNumber,
            @RequestParam("token") String token,
            @RequestParam("country") String country,
            @RequestParam("format") String format
    );

    @GetMapping(value = DiscogsUrls.AUTH_PATH, consumes = "application/x-www-form-urlencoded")
    String requestToken(
            @RequestHeader("Authorization") OAuth auth,
            @RequestHeader("User-Agent") String userAgent
    );
}
