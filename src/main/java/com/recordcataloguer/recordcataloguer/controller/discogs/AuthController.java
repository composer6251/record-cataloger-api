package com.recordcataloguer.recordcataloguer.controller.discogs;

import com.recordcataloguer.recordcataloguer.constants.LocalHostUrls;
import com.recordcataloguer.recordcataloguer.service.discogs.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = { LocalHostUrls.LAPTOP_IP, LocalHostUrls.FLUTTER_EMULATOR }) // Allow from Flutter app
public class AuthController {

    // Autowired to use Feign
    @Autowired
    private AuthService authService;

    @GetMapping(value = "/authenticate/feign")
    public String authenticate() {
        log.debug("Request received to authenticate User with Discogs");

        return authService.getAuthorizationUrlFeign();
    }

    @GetMapping(value = "/get-access-token")
    public String getAccessToken() {
        log.debug("Request received to authenticate User with Discogs");

        return authService.getAccessTokenFeign();
    }

    @GetMapping(value = "/verify-user-identity")
    public String verifyUserIdentity() {
        log.debug("Request received to authenticate User with Discogs");

        return authService.verifyIdentity();
    }
}
