package com.recordcataloguer.recordcataloguer.http.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsNewTokens;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class OAuth {
    private String oauth_consumer_key = DiscogsNewTokens.DISCOGS_CONSUMER_KEY;
    private String oauth_nonce = UUID.randomUUID().toString();
    private String oauth_signature = DiscogsNewTokens.DISCOGS_CONSUMER_SECRET +"%26";
    private String oauth_signature_method = "PLAINTEXT";
    private long oauth_timestamp = Instant.now().toEpochMilli();
    private String oauth_callback = "localhost:8080/api-test-page";
    private String oauth_version = "oauth_version=1.0";

    public static final String generateOAuthHeader( ) {

        OAuth oAuth = new OAuth();

        String auth = "OAuth oauth_consumer_key=\"" + oAuth.getOauth_consumer_key() + "\", " +
                "oauth_nonce=\"" + oAuth.getOauth_nonce() + "\", " +
                "oauth_signature=\"" + oAuth.getOauth_signature() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + oAuth.getOauth_timestamp() + "\", oauth_callback=\"" + oAuth.getOauth_callback() + "\"";

        return auth;
    }
}