package com.recordcataloguer.recordcataloguer.helpers.image;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ImageReaderTest {

    @Before
    public void init() {

    }

    public Map<String, String> initListImagePaths(){
        Map<String, String> testImagesWithCorrectResult = new HashMap<>();

        testImagesWithCorrectResult.put("src/test/resources/images/AR 34001 kind of blurry.jpg", "AR 340018");
        testImagesWithCorrectResult.put("src/test/resources/images/AR 34001 blurry.jpg", "AR 340018");
        testImagesWithCorrectResult.put("src/test/resources/images/AR 34001 very blurry.jpg", "AR 340018");
        testImagesWithCorrectResult.put("src/test/resources/images/AR 34001 obscured.jpg", "AR 340018");

        return testImagesWithCorrectResult;
    }

    /***
     * THIS TEST IS ONLY FOR VERIFYING HOW ACCURATE THE GOOGLE API IS FOR EXTRACTING TEXT FROM IMAGES OF VARIOUS
     * IMAGE QUALITIES. SHOULD NOT BE RUN EXCEPT DURING DEVELOPMENT
     * @return
     */

    private void testImage(){

    }
    @Test
    public void imageReaderShouldAccuratelyExtractTextFromAllImages() throws IOException {

        // Iterate testImages
        // Call detectText with each map key
        // Store result, either text from image or error message
        // If result is error, break and fail test?
        // else assert result = testImages.getValue
        Map<String, String> testImagesMap = initListImagePaths();
        List<String> result;
        List<String> failedImages = new ArrayList<>();
        // todo: Mock response from Google
        for (Map.Entry<String, String> testImages : testImagesMap.entrySet()) {
            try {
                result = ImageReader.detectText(testImages.getKey());
            }
            catch(IOException exception) {
                failedImages.add(testImages.getKey());
                failedImages.forEach(System.out::println);
                throw new IOException("Exception detecting image text for image: " + testImages.getKey() + "\nwith error: "
                        + exception.getMessage() + ".\n failing test");
                //TODO: Fail test or add failures to List?
            }

            System.out.println(failedImages.size());
            assertEquals(testImages.getValue(), result);
        }
    }
    // Loop through BIG array of pictures and verify that all images have text extracted correctly
    // Maybe unnecessary as that is testing Googles API???

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
