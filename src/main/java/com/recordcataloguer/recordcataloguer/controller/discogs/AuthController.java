//package com.recordcataloguer.recordcataloguer.controller.discogs;
//
//import com.recordcataloguer.recordcataloguer.service.discogs.AuthService;
//import com.recordcataloguer.recordcataloguer.service.discogs.DiscogsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@Slf4j
//@CrossOrigin(origins = {"http://10.116.244.134", "http://localhost:3000"}) // Allow from Flutter app
//public class AuthController {
//
//    // TODO: Autowired if you want feign
//    private AuthService authService;
//
//    @GetMapping(value = "/authenticate/feign")
//    public String authenticate() {
//        log.debug("Request received to authenticate User with Discogs");
//        return authService.getAuthorizationUrlFeign();
//    }
//}
