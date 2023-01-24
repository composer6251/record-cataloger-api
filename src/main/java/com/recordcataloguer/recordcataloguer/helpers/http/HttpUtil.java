package com.recordcataloguer.recordcataloguer.helpers.http;

import com.recordcataloguer.recordcataloguer.constants.DiscogsUrls;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.recordcataloguer.recordcataloguer.constants.DiscogsUrls.*;

@Slf4j
public class HttpUtil {

    public static final String generateUserAuthorizationUrl(String token) {
        //TODO: UNCOMMENT FOR HTTPHELPER PATH INSTEAD OF FEIGN. REFACTOR THOUGH.....
//        Map<String, String> oauthMap = getOAuthInfoFromDiscogsAuthResponse(token);
//        String oauthToken = oauthMap.get("oauth_token");
        return AUTHORIZATION_URL + "?oauth_token=" + token;
    }

    public static final Map<String, String> getOAuthInfoFromDiscogsAuthResponse(String token) {
        // Todo: Better way to create map from response token?
        // Use loop to add int i = key and i + 1 then i++
        String[] oauthToken = token.split("&|=");
        Map<String, String> oauthMap = new HashMap<>();
        oauthMap.put(oauthToken[0], oauthToken[1]);
        oauthMap.put(oauthToken[2], oauthToken[3]);
//        oauthMap.put(oauthToken[4], oauthToken[5]);

        for (String val : oauthToken) {
            log.info(val);
        }

        return oauthMap;
    }
}
