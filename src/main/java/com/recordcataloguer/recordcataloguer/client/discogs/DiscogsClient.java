package com.recordcataloguer.recordcataloguer.client.discogs;

import com.recordcataloguer.recordcataloguer.dto.DiscogsSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "discogsService", url = DiscogsUrls.SEARCH_BASE_URL)
public interface DiscogsClient {

    @GetMapping(value = DiscogsUrls.SEARCH_PATH, consumes = "application/json")
    ResponseEntity<DiscogsSearchResponse> getUSDiscogsRecordsByCategoryNumber(
            @RequestParam("token") String token,
            @RequestParam("catNo") String catNo,
            @RequestParam("country") String country
    );
}
