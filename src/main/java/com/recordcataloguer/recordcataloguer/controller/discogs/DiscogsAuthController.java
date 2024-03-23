package com.recordcataloguer.recordcataloguer.controller.discogs;

import com.recordcataloguer.recordcataloguer.constants.LocalHostUrls;
import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = { LocalHostUrls.LAPTOP_IP, LocalHostUrls.FLUTTER_EMULATOR }) // Allow from Flutter app
public class DiscogsAuthController {

    @Autowired
    private DiscogsAuthService discogsAuthService;

    @GetMapping(value = "user/authenticate")
    public String userAuthenticate(@RequestParam String userName, @RequestParam String ip) {
        log.debug("Request received to authenticate User with Discogs");

        return discogsAuthService.generateUserAuthentication();
    }

    @GetMapping(value = "user/authenticateAccessToken")
    public String getRequestToken(@RequestParam String userName, @RequestParam String ip) {
        log.debug("Request received to authenticate User with Discogs");

        return discogsAuthService.generateUserAuthentication();
    }


    @GetMapping(value = "/get-access-token")
    public String getAccessToken() {
        log.debug("Request received to authenticate User with Discogs");

        return discogsAuthService.getAccessTokenFeign();
    }

    @GetMapping(value = "/verify-user-identity")
    public String verifyUserIdentity(@RequestParam String username) {
        log.debug("Request received to authenticate User with Discogs");

        return discogsAuthService.verifyIdentity(username);
    }
}
