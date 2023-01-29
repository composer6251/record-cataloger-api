package com.recordcataloguer.recordcataloguer.dto.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsTokens;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class OAuth {
    private String oauth_consumer_key = DiscogsTokens.DISCOGS_CONSUMER_KEY;
    private String oauth_nonce = UUID.randomUUID().toString();
    private String oauth_signature = DiscogsTokens.DISCOGS_CONSUMER_SECRET + "&";
    private String oauth_signature_method = "PLAINTEXT";
    private long oauth_timestamp = Instant.now().toEpochMilli();
    private String oauth_callback = "localhost:8080/api-test-page";
    private String oauth_version = "oauth_version=1.0";
    private String oauth_verifier;
}