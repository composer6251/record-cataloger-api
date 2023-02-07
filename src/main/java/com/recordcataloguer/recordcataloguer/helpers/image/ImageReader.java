package com.recordcataloguer.recordcataloguer.helpers.image;

import com.google.cloud.vision.v1.*;
import com.recordcataloguer.recordcataloguer.helpers.discogs.DiscogsServiceHelper;
import com.recordcataloguer.recordcataloguer.helpers.regex.ImageReaderRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<String> extractCatalogueNumberFromImage(String imageUrl) {
        String text =
                this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));

        List<String> catalogueNumbers = ImageReaderRegex.extractRecordCatalogueNumber(text);

        return catalogueNumbers;
    }

//    public String filterText(String imageUrl) {
//        String text =
//                this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));
//
////        List<DiscogsSearchAlbumRequest> searchAlbumRequests = ImageReaderRegex.getCatalogNumberAndTitlesMap(text);
//
//        return text;
//    }

    // TODO: Maybe use annotatedResponse and vertices to determine location in picture. i.e. if it's top, and not a catNo, cat number maybe at the bottom
    // TODO: Better regexing??
    // TODO: More user burden?

    private void updateAlbumString(EntityAnnotation entityAnnotation, EntityAnnotation entityAnnotation2) {
        if(compareVerts(entityAnnotation.getBoundingPoly().getVertices(0).getX(), entityAnnotation2.getBoundingPoly().getVertices(0).getX())) {
            currentAlbumString.append(" " + entityAnnotation2.getDescription());
        }
    }
    private boolean compareVerts(int vert1, int vert2) {
        Integer diff = vert1 - vert2;
        log.info("Difference between x axes {} pt1: {} pt2: {} ", diff, vert1, vert2);
        return diff < 30 && diff > -30;
    }
    StringBuilder currentAlbumString = new StringBuilder();

    public String extractTextFromImage(String imageURL) {
        AnnotateImageResponse annotateImageResponse = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(imageURL), Feature.Type.TEXT_DETECTION);
        // List must be mutable in order to remove matched annotations
        List<EntityAnnotation> annotations = new ArrayList<>(annotateImageResponse.getTextAnnotationsList());

        DiscogsServiceHelper.getIndexesOfAlbums(annotations);
        DiscogsServiceHelper.getNextAlbumAnnotations(annotations);

        DiscogsServiceHelper.getAlbumsForLookup(annotations, 10);

        Map<Integer, String> albumMap = DiscogsServiceHelper.populateMap(annotations, 50);


        List<NormalizedVertex> test = annotations.get(0).getBoundingPoly().getNormalizedVerticesList();
        // Get base vertices: X-max, X-min, Y-max, y-min
        // get closest values for each, if with 400, then it is the next record and not random picture text
        // Compare start with the highest x: This is the right most record

        EntityAnnotation initialAnnotation = annotations.get(0);
        BoundingPoly initialBoundingPoly = initialAnnotation.getBoundingPoly();
        Integer initialXVert = initialBoundingPoly.getVertices(0).getX();

        Vertex farthestRightAlbumVertex = Vertex.newBuilder().build();
        Vertex currentAlbumVertex = Vertex.newBuilder().build();
        List<String> albumStrings = new ArrayList<>();
        Map<Integer, String> albumStringsMap = new HashMap<>();
        // TODO: Filter map entries by doing a map.entry.fulltext.contains(partialText)
        // TODO: Can each value be separated on whether olphanumeric/numeric(catno) or alpha only(title)
        // TODO: Print out vertices list to see what it is
        // TODO: print out each annotation vertices and compare difference
        // TODO:
        List<Integer> foundAnnotationIndices = new ArrayList<>();
        foundAnnotationIndices.add(0);
        for (Integer i = 1; i < annotations.size(); i++) {

            Iterator<EntityAnnotation> annotationsIterator = annotations.listIterator(i);

            StringBuilder currentAlbum = new StringBuilder();
            int finalI = i;
            // filter by index of
            // removeIf() ?
            // iterator directly
            // For each element
            //      for each compare
            //         true ?
               //      build string
                //     add string to map
                //      add matched element to list
                //      continue
            //      for each next
            //      if iter.annotations contains matchedElemList
            //          continue or filter(c2 -> matchedElemList.contains(iter.annotations.getIndexOf(c2))
                //      false ?
            //
            annotations.stream().filter(c -> !foundAnnotationIndices.contains(annotations.indexOf(c))).forEachOrdered(c -> {
//                annotations.removeIf(annotations.)
//                StringBuilder str = new StringBuilder();
//                if(!currentAlbum.isEmpty()) currentAlbum.delete(0, str.length() - 1);
                annotations.forEach(
                        c2 -> {
//                            if(foundAnnotationIndices.contains(annotations.indexOf(c2)))    continue;
                            if(compareVerts(c.getBoundingPoly().getVertices(0).getX(), c2.getBoundingPoly().getVertices(0).getX())) {
                                log.info("Difference between x axis points {} and {}, updating album entry number {} with value {}",
                                        c.getBoundingPoly().getVertices(0).getX(), c2.getBoundingPoly().getVertices(0).getX(),
                                        c2.getDescription());
                                currentAlbum.append(" ").append(c2.getDescription());
                                foundAnnotationIndices.add(annotations.indexOf(c2));

                            }
                        });
//                if(!currentAlbum.isEmpty()) currentAlbum.delete(0, currentAlbum.length() - 1);
            }

            );
                albumStringsMap.put(++finalI, currentAlbum.toString());

            log.info("Done with map entry for album number {} with value\n{}", finalI, currentAlbum);
        }

        return this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageURL));
    }

//    private String buildAlbumString(List<EntityAnnotation> annotations, EntityAnnotation initialAnnotation, BoundingPoly initialBoundingPoly) {
    //            List<EntityAnnotation> album = annotations.stream()
//                    .skip(1)
//                    .filter(c -> (c.getBoundingPoly().getVertices(0).getX()) - (initialXVert - (40 * i.get())) <= 0)
//                    .peek(c -> log.info("current vert {} next vert {}", c.getBoundingPoly().getVertices(0).getX()))
////                    .map(c -> c.getDescription())
//                    .collect(Collectors.toList());
////                    .flatMap(c -> currentAlbumString.append(currentAnnotation.getDescription()))
//            log.info("");

//            // TODO: Make sure this is comparing correctly
//            if (initialAnnotation.equals(currentAnnotation)) {
//                initialAnnotation = annotations.get(i);
//                if (initialAnnotation.hasBoundingPoly()) {
//                    initialBoundingPoly = initialAnnotation.getBoundingPoly();
//                }
//                farthestRightAlbumVertex = initialBoundingPoly.getVertices(0);
//                currentAlbumVertex = farthestRightAlbumVertex;
//
//                continue;
//            }
//            if (farthestRightAlbumVertex.getX() == 0) {
//                return null;
//            }
//
//            // current x is close enough to be same album, append string
//            if (currentBoundingPoly.getVertices(0).getX() - nextBoundingPoly.getVertices(0).getX() <= 10) {
//
//                currentAlbumString.append(currentAnnotation.getDescription());
//            }
//            if (i == annotations.size()) {
//                albumStrings.add(currentAlbumString.toString());
//                albumStringsMap.put(i, currentAlbumString.toString());
//            }
//            continue;
//        for (int i = 0; i < annotations.size() - 1; i++) {
//            boolean nextAlbumFound = false;
//            EntityAnnotation currentAnnotation = annotations.get(i);
//            EntityAnnotation nextAnnotation = annotations.get(++i);
//            BoundingPoly currentBoundingPoly = currentAnnotation.getBoundingPoly();
//            BoundingPoly nextBoundingPoly = nextAnnotation.getBoundingPoly();
//
//            StringBuilder currentAlbumString = new StringBuilder();
//
//            // TODO: Make sure this is comparing correctly
//            if(initialAnnotation.equals(currentAnnotation)) {
//                initialAnnotation = annotations.get(i);
//                if(initialAnnotation.hasBoundingPoly()) {
//                    initialBoundingPoly = initialAnnotation.getBoundingPoly();
//                }
//                farthestRightAlbumVertex = initialBoundingPoly.getVertices(0);
//                currentAlbumVertex = farthestRightAlbumVertex;
//
//                continue;
//            }
//            if(farthestRightAlbumVertex.getX() == 0) return null;
//
//            // current x is close enough to be same album, append string
//            if(currentBoundingPoly.getVertices(0).getX() - nextBoundingPoly.getVertices(0).getX() <= 10) {
//
//                currentAlbumString.append(currentAnnotation.getDescription());
//            }
//            if(i == annotations.size()) {
//                albumStrings.add(currentAlbumString.toString());
//                albumStringsMap.put("album " + String.valueOf(i), currentAlbumString.toString())
//            }
//            continue;
//            // current x is NOT close enough
//            // boolean nextAlbumFound = false;
//            // if x-vertex is between highest x vertex + 10 and highest x vertex - 10
//            //      StringBuilder albumString = new StringBuilder()
//            //      albumString.append(description)
//            // else {
//            //      if(!nextAlbumFound)
//            //              String nextAlbumVertex = currentEntityAnnotation.getVertexX
//            //      }
//            // i++
//
//        }
//    }

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