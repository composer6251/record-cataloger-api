package com.recordcataloguer.recordcataloguer.http.discogs;

import com.recordcataloguer.recordcataloguer.auth.DiscogsNewTokens;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OAuth {
    private String oauth_consumer_key = DiscogsNewTokens.DISCOG_APP_OAUTH_KEY;
    private String oauth_nonce = LocalDateTime.now().toString();
    private String oauth_signature = DiscogsNewTokens.DISCOG_APP_OAUTH_SECRET+"&";
    private String oauth_signature_method = "PLAINTEXT";
    private String oauth_timestamp = LocalDateTime.now().toString();
    private String oauth_callback;
}