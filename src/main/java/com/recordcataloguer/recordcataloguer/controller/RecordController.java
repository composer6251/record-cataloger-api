package com.recordcataloguer.recordcataloguer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:62440") // Allow from app
public class RecordController {

    @GetMapping("/test")
    public void testEndpoint(){
        log.error("API!!!!");
    }

}
