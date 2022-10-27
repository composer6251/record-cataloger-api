package com.recordcataloguer.recordcataloguer.helpers.image;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.recordcataloguer.recordcataloguer.helpers.regex.ImageReaderRegex;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
public class ImageReader {

//    public static void main(String[] args) throws IOException {
//        detectText();
//    }
//    public static void detectText() throws IOException {
//        String filePath = "src/main/resources/images/kindaBlurry.jpg";
//        detectText(filePath);
//    }

    // Detects text in the specified image.
    public static List<String> detectText(String filePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        ImageAnnotatorClient client = ImageAnnotatorClient.create();
        try {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            // TODO: Don't I only need one?
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    client.close();
                    throw new IOException("Error extracting image text for image path: "+ filePath + " with error: " + res.getError().getMessage());
                }

                // TODO: Do I need a loop?
                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    List<String> catalogueNumber = ImageReaderRegex.extractRecordCatalogueNumber(annotation.getDescription());
                    client.close();
                    return catalogueNumber;
                }
            }
        }
        catch(IOException exception) {
            throw new IOException("Error extracting image text for image path: "+ filePath + " with error: " + exception.getMessage());
        }
        finally {
            client.close();
        }

        throw new IOException("Error extracting image text for image path: "+ filePath);
    }
}