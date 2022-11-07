package com.recordcataloguer.recordcataloguer.service.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.http.discogs.DiscogsSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DiscogsService {

    @Autowired
    DiscogsClient discogsClient;
    private String catNo = "AR 34001";
    private String country = "US";

    public ResponseEntity<DiscogsSearchResponse> lookupUpRecordByCatalogueNumber(){

        ResponseEntity<DiscogsSearchResponse> discogsSearchResponse =
                discogsClient.getUSDiscogsRecordsByCategoryNumber(DiscogsTokens.DISCOGS_PERSONAL_ACCESS_TOKEN, catNo, country);
        return discogsSearchResponse;
    }

    // Make UI consumable object

    // Filter unique artist/title
    // Return first result?
}
