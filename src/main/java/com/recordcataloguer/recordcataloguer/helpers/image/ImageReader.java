package com.recordcataloguer.recordcataloguer.helpers.image;

import com.google.cloud.vision.v1.*;
import com.recordcataloguer.recordcataloguer.helpers.regex.ImageReaderRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Google Responses
 **** Empty String
 * "code": 400,
 *     "message": "Request must specify image and features.",
 *     "status": "INVALID_ARGUMENT"
 *
 * ****File not found
 *   "error": {
 *         "code": 5,
 *         "message": "Error opening file: gs://cloud-samples-data/vision/ocr/sig."
 *
 * ****Server Error
 * 500
 */
@Service
@Slf4j
public class ImageReader {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    // get text from image
    public List<String> extractTextFromImage(String imageUrl) {
        String text =
                this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));

        List<String> catalogueNumbers = ImageReaderRegex.extractRecordCatalogueNumber(text);

        return catalogueNumbers;
    }

    // Get labels from single Image
    public List<String> extractLabelFromImage(String imageUrl) {

        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(
                resourceLoader.getResource(imageUrl)
        );

        List<EntityAnnotation> annotations = response.getLabelAnnotationsList();
        List<String> imageLabels = new ArrayList<>();

        return imageLabels;
    }

    // get text from PDF

    /*******GOOGLE CLOUD VISION API IMPLEMENTATION****************************/


//    public static List<String> detectText(String filePath) throws IOException {
//        List<AnnotateImageRequest> requests = new ArrayList<>();
//
//        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
//
//        Image img = Image.newBuilder().setContent(imgBytes).build();
//        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
//        AnnotateImageRequest request =
//                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
//        requests.add(request);
//
//        // Initialize client that will be used to send requests. This client only needs to be created
//        // once, and can be reused for multiple requests. After completing all of your requests, call
//        // the "close" method on the client to safely clean up any remaining background resources.
//        ImageAnnotatorClient client = ImageAnnotatorClient.create();
//        try {
//            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
//            // TODO: Don't I only need one?
//            List<AnnotateImageResponse> responses = response.getResponsesList();
//            for (AnnotateImageResponse res : responses) {
//                if (res.hasError()) {
//                    System.out.format("Error: %s%n", res.getError().getMessage());
//                    client.close();
//                    throw new IOException("Error extracting image text for image path: "+ filePath + " with error: " + res.getError().getMessage());
//                }
//
//                // TODO: Do I need a loop?
//                // TODO: Implementation of List<String>
//                // For full list of available annotations, see http://g.co/cloud/vision/docs
////                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
////                    List<String> catalogueNumber = ImageReaderRegex.extractRecordCatalogueNumber(annotation.getDescription());
////                    client.close();
////                    return catalogueNumber;
////                }
//                //TODO: implementation of String
//                log.info("Result from Cloud Vision for file {} \n{}", filePath, res.getTextAnnotationsList().get(0));
//                if(!res.getTextAnnotationsList().isEmpty()) {
////                    log.info("Result from Cloud Vision for file {} \n{}", filePath, res.getTextAnnotationsList().get(0));
//                    return ImageReaderRegex.extractRecordCatalogueNumber(res.getTextAnnotationsList().get(0).getDescription());
//                }
//                //
//                // APP IDEAS
//                // Take picture of multiple albums
//                // Display all albums found
//                // User selects which ones to add to collection
//                // User looks at collection and has the option (if UPC exists) to print out barcode with UPC
//
//            }
//        }
//        catch(IOException exception) {
//            throw new IOException("Error extracting image text for image path: "+ filePath + " with error: " + exception.getMessage());
//        }
//        finally {
//            client.close();
//        }
//
//        throw new IOException("Error extracting image text for image path: "+ filePath);
//    }
}