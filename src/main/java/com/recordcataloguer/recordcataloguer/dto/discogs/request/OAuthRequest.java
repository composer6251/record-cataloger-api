package com.recordcataloguer.recordcataloguer.dto.discogs.request;

import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.OAuthRequestConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class OAuthRequest {
    private String oauth_consumer_key = DiscogsTokens.DISCOGS_CONSUMER_KEY;
    private String oauth_nonce = RandomStringUtils.random(9, false, true);
    private String oauth_signature = DiscogsTokens.DISCOGS_CONSUMER_SECRET + "&" + DiscogsTokens.DISCOGS_OAUTH_TOKEN;
    private String oauth_signature_method = OAuthRequestConstants.OAUTH_NONCE_PLAINTEXT;
    private String oauth_timestamp = String.valueOf(Instant.now().getEpochSecond());
    private String oauth_callback = OAuthRequestConstants.OAUTH_CALLBACK_PHONE;
    private String oauth_version = OAuthRequestConstants.OAUTH_VERSION_1_0;
    private String oauth_verifier = DiscogsTokens.MY_OAUTH_VERIFIER_TOKEN;
}