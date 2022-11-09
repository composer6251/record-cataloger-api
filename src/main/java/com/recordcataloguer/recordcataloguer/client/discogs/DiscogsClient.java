package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.config.FeignConfiguration;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsResultDTO;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "discogsService", url = DiscogsUrls.SEARCH_BASE_URL, configuration = FeignConfiguration.class)
public interface DiscogsClient {

    @GetMapping(value = DiscogsUrls.SEARCH_PATH, consumes = "application/json")
    DiscogsResultDTO getUSDiscogsRecordByCategoryNumber(
            @RequestParam("catno") String catalogueNumber,
            @RequestParam("token") String token,
            @RequestParam("country") String country,
            @RequestParam("format") String format
    );

    @GetMapping(value = DiscogsUrls.SEARCH_PATH, consumes = "application/json")
    DiscogsSearchResponse getDiscogsRecordByCategoryNumber(
            @RequestParam("catno") String catalogueNumber,
            @RequestParam("token") String token,
            @RequestParam("country") String country,
            @RequestParam("format") String format
    );
}
