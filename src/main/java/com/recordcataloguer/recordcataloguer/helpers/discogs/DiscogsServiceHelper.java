package com.recordcataloguer.recordcataloguer.helpers.discogs;

import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;
import com.recordcataloguer.recordcataloguer.dto.vision.AlbumAnnotation;
import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import static com.recordcataloguer.recordcataloguer.helpers.regex.VisionTextFiltering.*;

@Slf4j
public class DiscogsServiceHelper {

    // Filter out catalogue numbers by strict comparison. ie. 2 or more alpha - 4 or more numbers, or " " instead of dash
    // Filter out Titles by
    // 1. Alpha Alpha
    //  2. Alpha single
    //  3. Ignore record labels by contains COMMON_LABEL_WORDS
    // Store suspected artists/title
    // Ask discogs if they are real

    public static List<DiscogsSearchAlbumRequest> buildSearchRequestsFromIndividualStrings(List<String> textsFromAlbums, String regex) {

        List<DiscogsSearchAlbumRequest> albumsToSearch = new ArrayList<>();
        for (String text : textsFromAlbums) {

            albumsToSearch.add(buildSingleRequest(text, regex));

        }
            return albumsToSearch;
    }
    public static List<DiscogsSearchAlbumRequest> buildSearchRequestsFromRawText(String rawText, String regex) {

        List<DiscogsSearchAlbumRequest> albumsToSearch = new ArrayList<>();


        albumsToSearch.add(buildSingleRequest(rawText, regex));


        return albumsToSearch;
    }

    public static DiscogsSearchAlbumRequest buildSingleRequest(String rawText, String regex) {

        Pattern regexToUse = Pattern.compile(regex);
        Pattern CAT_NO_WITHOUT_SPACE = Pattern.compile(CAT_NO);

        DiscogsSearchAlbumRequest albumRequest = new DiscogsSearchAlbumRequest();
        Matcher matchedText = regexToUse.matcher(rawText);
        List<String> matches = new ArrayList<>();
        albumRequest.setTitle(rawText);
        albumRequest.setOriginalString(rawText);

        while(matchedText.find()){
            matches.add(matchedText.group());
        }
        if(matches.size() == 0) {
            Matcher noDashOrSpace = CAT_NO_WITHOUT_SPACE.matcher(rawText);

            while(noDashOrSpace.find()){
                matches.add(noDashOrSpace.group());
            }
        }
        // maybeCatNo StringUtils.isNumericSpace (123 12323)
        // If 1 match that contains a dash, then that is the only thing we have got to go on, but it should be the catalog number
        if(matches.size() == 1 && StringUtils.contains(matches.get(0), "-")) {
            albumRequest.setCatNo(matches.get(0));
        }
        if(matches.size() == 1 && !StringUtils.contains(matches.get(0), "-")) {
            albumRequest.setCatNo(matches.get(0));
            albumRequest.setTitle(rawText);
        }
        // TODO: Implement
        if(matches.size() > 1){

        }

        albumRequest.setConfidenceLevel(getConfidenceLevel(albumRequest.getCatNo(), albumRequest.getTitle()));

        // If there's no catNo and the title is an unrealistic length for an album OR the catno is < 5 characters, it is probably not accurate
        if((StringUtils.isBlank(albumRequest.getCatNo()) && albumRequest.getOriginalString().length() < 10)
                || (!StringUtils.isBlank(albumRequest.getCatNo()) && albumRequest.getCatNo().length() < 4));

        return albumRequest;

    }


    private static int getConfidenceLevel(String maybeCatNo, String maybeTitle) {
        /**values to determine accuracy of search**/
        boolean maybeTitleHasMoreWhiteSpaces = StringUtils.countMatches(maybeTitle, StringUtils.SPACE) > StringUtils.countMatches(maybeCatNo, StringUtils.SPACE);
        boolean maybeTitleIsAlphaOnly = StringUtils.isAlpha(StringUtils.remove(maybeTitle, " "));
        boolean maybeTitleContainsCommonWord = COMMON_TITLE_WORDS.contains(StringUtils.split(maybeTitle));
        boolean maybeCatNoContainsDash = StringUtils.contains(maybeCatNo, "-");

        int confidenceLevel = 0;
        if(maybeTitleHasMoreWhiteSpaces) confidenceLevel += 40;
        if(maybeTitleIsAlphaOnly) confidenceLevel += 30;
        if(maybeTitleContainsCommonWord) confidenceLevel += 20;
        if(maybeCatNoContainsDash) confidenceLevel += 40;

        return confidenceLevel;
    }
    // TODO: CAN I DETERMINE FROM SPINETEXTS WHAT IS TITLE AND WHAT IS CATNO?
    public static List<String> getSearchStringsByImageVertices(List<EntityAnnotation> annotations) {
        List<AlbumAnnotation> albumAnnotations;
        annotations.sort(Comparator.comparing(a -> getVerticesTotal(a.getBoundingPoly(), 'X')));
        // Get album annotations with initial XVertex and totals for X and Y Vertices
        albumAnnotations = annotations
                .stream()
                .map(a -> {
                    int xVert1 = a.getBoundingPoly().getVertices(0).getX();
//                    int firstYVertex = a.getBoundingPoly().getVertices(0).getY();
                    int xVert2 = a.getBoundingPoly().getVertices(1).getX();
                    int xVert3 = a.getBoundingPoly().getVertices(2).getX();
                    int xVert4 = a.getBoundingPoly().getVertices(3).getX();

                    int xTotal = getVerticesTotal(a.getBoundingPoly(), 'X');
                    int yTotal = getVerticesTotal(a.getBoundingPoly(), 'Y');

                    return new AlbumAnnotation(annotations.indexOf(a), a.getDescription(), xTotal, yTotal, xVert1, xVert2, xVert3, xVert4);
                }).collect(Collectors.toList());

        // Sort the albumAnnotations by firstXVert, to get horizontal ordering, then sort horizontal ordering by yVertices to vertical aspect
        albumAnnotations.sort(Comparator.comparing(AlbumAnnotation::getFirstXVert).thenComparing(AlbumAnnotation::getYVerticesSum).reversed());

        List<String> albumSpineTexts = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        // Compare current and next albumAnnotation to determine if they belong on the same album spine
        int separationDistance = 20;
        for (int i = 0; i < albumAnnotations.size() - 1; i++) {
            AlbumAnnotation current = albumAnnotations.get(i);
            AlbumAnnotation next = albumAnnotations.get(i + 1);
            // TODO: Add string to list, because we do not know if the texts are in the right order on the spine, but we can check by YVertices
            // TODO: Give user option to pick separator distance
            // TODO: Maybe option to specify number of records
            stringBuilder.append(" ").append(current.getDescription());
            // if the xVertices are too far apart, then they are likely on different album spines
            boolean isSameAlbum = Math.abs(next.getFirstXVert() - current.getFirstXVert()) <= separationDistance
                    || Math.abs(next.getSecondXVert() - current.getSecondXVert()) <= separationDistance
                    || Math.abs(next.getThirdXVert() - current.getThirdXVert()) <= separationDistance
                    || Math.abs(next.getFourthXVert() - current.getFourthXVert()) <= separationDistance;
            // If it's probably the same, we've already appended the description for current, move on to next iteration.
            // TODO: Compare EACH x vert and if ANY are within 30, then it's the same album
            if(isSameAlbum) {
                continue;
            }

            // We are on the next album so add spineString to list and reset stringBuilder
            albumSpineTexts.add(stringBuilder.toString());
            try {
            stringBuilder.delete(0, stringBuilder.length());

            }catch (IndexOutOfBoundsException exception) {
                System.out.println(exception.getMessage());
            }

        }

        return albumSpineTexts;
    }
    // Take in strinngs
    // Split on (|)
    // Check if check if contains catNo format
    // extract maybe title
    // Build requests from there
    public static void filterAlbumSpineText(List<String> spineText) {

        for (String text: spineText) {
            List<String> maybeCatNos = new ArrayList<>();
            maybeCatNos.addAll(searchForString(text, CAT_NO_SIX_AND_GREATER));
            // maybeTitle = regex alpha + " " alpha
            String[] splitString = text.split("|");

        }

    }
    private static List<String> searchForString(String searchText, String regex) {
        Pattern pattern = Pattern.compile(regex);

        Matcher matchedText = pattern.matcher(searchText);
        List<String> matches = new ArrayList<>();

        while(matchedText.find()){
            matches.add(matchedText.group());
        }
        return matches;
    }

    public static List<EntityAnnotation> getNextAlbumAnnotations(List<EntityAnnotation> annotations) {
        List<EntityAnnotation> nextAlbum = new ArrayList<>();
//        annotations.get(0).getBoundingPoly()
        List<EntityAnnotation> sortedAnnotations = annotations.stream().sorted(Comparator.comparing(a -> a.getBoundingPoly().getVertices(0).getX())).toList();
        StringBuilder str = new StringBuilder();
        List<String> albumStrings = new ArrayList<>();
        Map<Integer, String> test = new HashMap<>();
        List<Integer> indicesProcessed = new ArrayList<>();

        //TODO: Need to use all four values of XVert to determine which order to put them in
        // Add all four Xvert together on current and next
        // We want to start at the 1st index and 0 is a summary of verts
        for (int i = 1; i < sortedAnnotations.size() - 1; i++) {
            EntityAnnotation current = sortedAnnotations.get(i);
            EntityAnnotation next = sortedAnnotations.get(i + 1);
            String curAndNextAnnInd = sortedAnnotations.indexOf(current) + " " +  sortedAnnotations.indexOf(next);
            String sort = annotations.indexOf(current) + " " +  annotations.indexOf(next);
            if(i == 1) str.append(current.getDescription());

            int currentXVert = current.getBoundingPoly().getVertices(0).getX();
            int nextXVert = next.getBoundingPoly().getVertices(0).getX();

            int currentXTotal = getVerticesTotal(current.getBoundingPoly(), 'X');
            int nextXTotal = getVerticesTotal(current.getBoundingPoly(), 'X');

            int currentYTotal = getVerticesTotal(current.getBoundingPoly(), 'Y');
            int nextYTotal = getVerticesTotal(current.getBoundingPoly(), 'Y');

            if(current.getDescription().contains("ANTIC")) {
                System.out.println();
            }
            // Need the y verts to determine order of description
            // create compare function for verts
            if((nextXTotal - currentXTotal) > Math.abs(150)) {
                str.append(" | ").append(current.getDescription());
                albumStrings.add(str.toString());

                // We don't want to add this to the album
                try{
                    str.delete(0,str.length() - 1);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                continue;
            }
            // if currentXverts > nextXverts str.append current.desc else
            //      if currentYverts > nextYverts
            test.put(i, current.getDescription());
            str.append(" | ").append(current.getDescription());
            nextAlbum.add(current);
            int cur = annotations.indexOf(current);
            indicesProcessed.add(i);
        }
        List<Integer> ordered = indicesProcessed.stream().sorted(Comparator.comparing(idx -> idx.intValue())).collect(Collectors.toList());
        return nextAlbum;
    }

    public static int getVerticesTotal(BoundingPoly boundingPoly1, char coord) {
        int verticesTotal = 0;

        for (int j = 0; j <= 3; j++) {
            if(coord == 'X'){
                verticesTotal += boundingPoly1.getVertices(j).getX();
            }
            if(coord == 'Y') {
                verticesTotal += boundingPoly1.getVertices(j).getY();
            }

        }
        return verticesTotal;
    }

    public static Map<Integer, String> getAlbumsForLookup(List<EntityAnnotation> annotations, int separatorDistance) {
        List<Integer> sortedXVertices = annotations.stream()
                .sorted(Comparator.comparing(a -> a.getBoundingPoly().getVertices(0).getX()))
                .map(a -> a.getBoundingPoly().getVertices(0).getX())
                .collect(Collectors.toList());

         List<EntityAnnotation> nextAlbum = getNextAlbumAnnotations(annotations);
//         List<EntityAnnotation>
         String albumText = nextAlbum.stream().map(EntityAnnotation::getDescription).collect(Collectors.joining(" /?/"));

//        final int BASELINE_X_VERT = annotations.get(0).getBoundingPoly().getVertices(0).getX();
//        int baselineXVert = annotations.get(1).getBoundingPoly().getVertices(0).getX();
//        List<EntityAnnotation> annotationList = new ArrayList<>();
//        List<Integer> spaceBetweenIndices = new ArrayList<>();


//        annotations.subList(1, annotations.size() - 1)
//                .stream()
//                .sorted(Comparator.comparing(a-> a.getBoundingPoly().getVertices(0).getX()))
//                .filter(a -> {
//                    int nextIndex = annotations.indexOf(a) + 1;
//                    EntityAnnotation next = annotations.get(nextIndex);
//                    if(a.getBoundingPoly().getVertices(0).getX() - next.getBoundingPoly().getVertices(0).getX() >= 70) {
//                        return nextIndex;
//                    }
//                    return;
//                }
//
//
//                )



        Map<Integer, String> albumMap = new HashMap<>();
        final int BASELINE_X_VERT = annotations.get(0).getBoundingPoly().getVertices(0).getX();
        annotations.subList(1, annotations.size() - 1)
                .stream()
                .sorted(Comparator.comparing(a -> a.getBoundingPoly().getVertices(0).getX()))
                .forEach(a -> {
                    int albumIndex = mapVisionTextsToAlbumsByXVert(BASELINE_X_VERT, a.getBoundingPoly().getVertices(0).getX(), separatorDistance);
                    if(albumMap.get(albumIndex) == null){
                        albumMap.put(albumIndex, " " + a.getDescription() + "("+a.getBoundingPoly().getVertices(0).getX()+")");
                    } else{
                        albumMap.put(albumIndex, albumMap.get(albumIndex) + " " + a.getDescription() + "("+a.getBoundingPoly().getVertices(0).getX()+")");
                    }
        });

        return albumMap;
    }

    public static int mapVisionTextsToAlbumsByXVert(int baselineXVert, int xVert, int separatorDistance) {
        int result = Math.absExact(baselineXVert - xVert);

        int albumIndexByXVert = result / separatorDistance;

        return ++albumIndexByXVert;


    }

    public static Map<Integer, String> populateMap(List<EntityAnnotation> annotations, int separatorDistance) {

        Map<Integer, String> albumMap = new HashMap<>();
        final int BASELINE_X_VERT = annotations.get(0).getBoundingPoly().getVertices(0).getX();
        annotations.subList(1, annotations.size() - 1).forEach(a -> {
            int albumIndex = mapVisionTextsToAlbumsByXVert(BASELINE_X_VERT, a.getBoundingPoly().getVertices(0).getX(), separatorDistance);
            if(albumMap.get(albumIndex) == null){
                    albumMap.put(albumIndex, " " + a.getDescription() + "("+a.getBoundingPoly().getVertices(0).getX()+")");
            } else{
                    albumMap.put(albumIndex, albumMap.get(albumIndex) + " " + a.getDescription() + "("+a.getBoundingPoly().getVertices(0).getX()+")");
            }
        });

        return albumMap;
    }


    public static List<String> getTextForIndividualAlbums(String visionResponse) {
        String[] textForIndividualAlbums = visionResponse.split("\n");

        return Arrays.stream(textForIndividualAlbums).toList();
    }

    public static List<String> getSixAndGreaterLengthCatNos(String rawVisionText, String regex) {
        // Look for sixAndGreater
        Pattern REGEX_ALPHANUMERIC = Pattern.compile(StringUtils.defaultString(regex, CAT_NO_SIX_AND_GREATER));
        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(rawVisionText);
        List<String> matches = new ArrayList<>();
        while(matchedText.find()){
            matches.add(matchedText.group());
        }

        return matches;
    }

//    public static List<DiscogsSearchAlbumRequest> getCatalogNumberAndTitlesMap(String visionText) {
//
//        List<String> individualTextForAlbums = getTextForIndividualAlbums(visionText);
//
//        List<DiscogsSearchAlbumRequest> albumSearchRequests = buildSearchRequestsFromIndividualStrings(individualTextForAlbums);
//
//        return albumSearchRequests;
//    }

    public static List<String> extractRecordCatalogueNumber(String text) {

        // Pass in regex and individual text for albums to method
        // result passes into get confidence level
        // Retry with different regex if needed

        List<String> individualTextForAlbums = getTextForIndividualAlbums(text);
        Map<String, String> albumInfo = new HashMap<>();

        Pattern REGEX_ALPHANUMERIC = Pattern.compile(CATALOG_NUMBER_REGEX);
        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(text);
        List<String> matches = new ArrayList<>();
        while(matchedText.find()){
            matches.add(matchedText.group());
        }

        return matches;
    }
}
