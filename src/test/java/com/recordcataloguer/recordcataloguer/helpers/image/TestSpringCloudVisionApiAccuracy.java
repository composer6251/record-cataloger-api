package com.recordcataloguer.recordcataloguer.helpers.image;//package com.recordcataloguer.recordcataloguer.helpers.image;

import java.io.IOException;
import java.util.*;

import com.recordcataloguer.recordcataloguer.helpers.image.vision.ImageReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages={
        "com.recordcataloguer.recordcataloguer", "com.recordcataloguer.recordcataloguer.application",
        "com.recordcataloguer.recordcataloguer", "com.recordcataloguer.recordcataloguer.application"})
public class TestSpringCloudVisionApiAccuracy {

    public static void main(String[] args) {

        SpringApplication.run(TestSpringCloudVisionApiAccuracy.class, args);
        extractTextFromImage();
    }
//    public static void main(String[] args) throws IOException {
//       imageReaderShouldAccuratelyExtractTextFromAllImages();
//  //      extractTextFromImage();
//    }

    /***
     * THIS TEST IS ONLY FOR VERIFYING HOW ACCURATE THE GOOGLE API IS FOR EXTRACTING TEXT FROM IMAGES OF VARIOUS
     * IMAGE QUALITIES. SHOULD NOT BE RUN EXCEPT DURING DEVELOPMENT
     * @return
     */

    public static void extractTextFromImage(){
        ImageReader imageReader = new ImageReader();

        System.out.println(imageReader.extractTextFromImage("src/test/resources/lovenotes/Note1.jpeg", 0));
    }
//    public static void imageReaderShouldAccuratelyExtractTextFromAllImages() throws IOException {
//
//        Map<String, String> testImagesMap = initListImagePaths();
//        List<String> maybeCatalogueNumbers = new ArrayList<>();
//        List<String> failedImages = new ArrayList<>();
//        List<String> successfulImages = new ArrayList<>();
//        ImageReader imageReader = new ImageReader();
//        ModelAndView modelAndView = new ModelAndView();
//
//        modelAndView = imageReader.extractTextFromImage("/Users/david/Coding Projects/record-cataloguer-api/src/test/resources/images/20221017_163141.jpg");
//        System.out.println(modelAndView);
//    }

    public static void imageReaderShouldAccuratelyExtractTextFromAllImages() throws IOException {

//        Map<String, String> testImagesMap = initListImagePaths();
//        List<String> maybeCatalogueNumbers = new ArrayList<>();
//        List<String> failedImages = new ArrayList<>();
//        List<String> successfulImages = new ArrayList<>();
        ImageReader imageReader = new ImageReader();
//        ModelAndView modelAndView = new ModelAndView();
        System.out.println(imageReader.extractTextFromImage("/Users/david/Coding Projects/record-cataloguer-api/src/test/resources/images/20221017_163141.jpg", 0));


    }
    /***
     * Only to be used when testing accuracy of Google API
     * @return
     */
    public static Map<String, String> initListImagePaths(){
          Map<String, String> testImagesWithCorrectResult = new HashMap<>();

        testImagesWithCorrectResult.put("src/test/resources/images/AR34001.jpg", "AR 34001");
        testImagesWithCorrectResult.put("/Users/david/Coding Projects/record-cataloguer-api/src/test/resources/images/AR34001.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101233.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101247.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101308.jpg", "AR 34001");
        testImagesWithCorrectResult.put("src/test/resources/images/20221025_101317.jpg", "AR 34001");

        return testImagesWithCorrectResult;
    }
}
