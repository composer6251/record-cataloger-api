package com.recordcataloguer.recordcataloguer.helpers.image;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ImageReaderTest {


    @Autowired
    ResourceLoader resourceLoader;

    @Test
    public void resourceLoaderShouldFindFile() {

        Resource resource = resourceLoader.getResource("/Users/david/Coding Projects/record-cataloguer-api/src/test/resources/images/20221017_163141.jpg");
        System.out.println(resource);
    }

    // Invalid input to detectText(String filePath)

    // Google returns 200
    // Extracted text or File not found
    // Google returns 400
    // Invalid file name, empty string or null path

    // File not found handled
    // Image client is closed?
    // NPEs caught

    // Correct text is grabbed and unnecessary text is discarded
}
