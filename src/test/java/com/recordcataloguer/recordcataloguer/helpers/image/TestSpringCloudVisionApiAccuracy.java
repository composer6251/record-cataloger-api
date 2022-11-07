package com.recordcataloguer.recordcataloguer.helpers.image;//package com.recordcataloguer.recordcataloguer.helpers.image;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class TestSpringCloudVisionApiAccuracy {

    public static void main(String[] args) throws IOException {
        imageReaderShouldAccuratelyExtractTextFromAllImages();
    }

    /***
     * THIS TEST IS ONLY FOR VERIFYING HOW ACCURATE THE GOOGLE API IS FOR EXTRACTING TEXT FROM IMAGES OF VARIOUS
     * IMAGE QUALITIES. SHOULD NOT BE RUN EXCEPT DURING DEVELOPMENT
     * @return
     */
    public static void imageReaderShouldAccuratelyExtractTextFromAllImages() throws IOException {

        Map<String, String> testImagesMap = initListImagePaths();
        List<String> maybeCatalogueNumbers = new ArrayList<>();
        List<String> failedImages = new ArrayList<>();
        List<String> successfulImages = new ArrayList<>();
        ImageReader imageReader = new ImageReader();
        ModelAndView modelAndView = new ModelAndView();

        modelAndView = imageReader.extractTextFromImage("/Users/david/Coding Projects/record-cataloguer-api/src/test/resources/images/20221017_163141.jpg");
        System.out.println(modelAndView);
    }
    /***
     * Only to be used when testing accuracy of Google API
     * @return
     */
    public static Map<String, String> initListImagePaths(){
          Map<String, String> testImagesWithCorrectResult = new HashMap<>();

        testImagesWithCorrectResult.put("src/test/resources/images/AR 34001 kind of blurry.jpg", "AR 34001");
        testImagesWithCorrectResult.put("/Users/david/Coding Projects/record-cataloguer-api/src/test/resources/images/AR 34001 kind of blurry.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101233.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101247.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101308.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101317.jpg", "AR 34001");

        return testImagesWithCorrectResult;
    }
}
