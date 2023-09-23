package com.recordcataloguer.recordcataloguer.dto.discogs.request;

import com.recordcataloguer.recordcataloguer.constants.auth.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.constants.auth.OAuthRequestConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class OAuthRequest {
    private String oauth_consumer_key = DiscogsTokens.DISCOGS_CONSUMER_KEY;
    private String oauth_nonce = UUID.randomUUID().toString();
    private String oauth_signature = DiscogsTokens.DISCOGS_CONSUMER_SECRET + "&";
    private String oauth_signature_method = OAuthRequestConstants.OAUTH_NONCE_PLAINTEXT;
    private long oauth_timestamp = Instant.now().toEpochMilli();
    private String oauth_callback = OAuthRequestConstants.OAUTH_CALLBACK_8080_API_TEST_PAGE;
    private String oauth_version = OAuthRequestConstants.OAUTH_VERSION_1_0;
    private String oauth_verifier;
}