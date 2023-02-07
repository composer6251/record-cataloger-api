package com.recordcataloguer.recordcataloguer.helpers.discogs;

import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Vertex;
import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;
import com.recordcataloguer.recordcataloguer.dto.vision.AlbumAnnotation;
import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import info.debatty.java.stringsimilarity.SorensenDice;
import org.apache.commons.lang3.StringUtils;

import static com.recordcataloguer.recordcataloguer.helpers.regex.VisionTextFiltering.CAT_NO_MOST_COMMON;

@Slf4j
public class DiscogsServiceHelper {

    public static List<EntityAnnotation> getIndexesOfAlbums(List<EntityAnnotation> annotations) {
        List<AlbumAnnotation> albumAnnotations;
        annotations.sort(Comparator.comparing(a -> a.getBoundingPoly().getVertices(0).getX()));
        albumAnnotations = annotations
                .stream()
                .map(a -> {
                    int firstXVertex = a.getBoundingPoly().getVertices(0).getX();
                    int firstYVertex = a.getBoundingPoly().getVertices(0).getY();
                    int xTotal = getVerticesTotal(a.getBoundingPoly(), 'X');
                    int yTotal = getVerticesTotal(a.getBoundingPoly(), 'Y');

                    return new AlbumAnnotation(annotations.indexOf(a), a.getDescription(), xTotal, yTotal, firstXVertex, firstYVertex);
                }).collect(Collectors.toList());

        albumAnnotations.sort(Comparator.comparing(AlbumAnnotation::getFirstXVert).thenComparing(AlbumAnnotation::getYVerticesSum).reversed());
        albumAnnotations.forEach(a -> log.info("Album Annotations {}, ",a ));
        List<String> albumSpineTexts = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < albumAnnotations.size() - 1; i++) {
            AlbumAnnotation current = albumAnnotations.get(i);
            AlbumAnnotation next = albumAnnotations.get(i + 1);

            stringBuilder.append("| ").append(current.getDescription());

            boolean isSameAlbum = Math.abs(next.getFirstXVert() - current.getFirstXVert()) <= 30;
            if(isSameAlbum) {
                continue;
            }
            // We are on the next album

            albumSpineTexts.add(stringBuilder.toString());
            try {
            stringBuilder.delete(0, stringBuilder.length());

            }catch (IndexOutOfBoundsException exception) {
                System.out.println(exception.getMessage());
            }

        }
        List<AlbumAnnotation> sortedAlbumAnnotations = albumAnnotations.stream()
                .sorted(Comparator.comparing(album -> album.getXVerticesSum()))
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        albums -> albums.stream().sorted(Comparator.comparing(album -> album.getYVerticesSum()))))
                .collect(Collectors.toList());

        albumAnnotations
                .sort(Comparator.comparing(AlbumAnnotation::getXVerticesSum).reversed().thenComparing(AlbumAnnotation::getYVerticesSum));




        return annotations;
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


    public static List<String> getSixAndGreaterLengthCatNos(String rawVisionText, String regex) {
        // Look for sixAndGreater
        // Then look up all

        // Or look for all alphanumeric patterns and then filter by size?

        // Can I deconstruct the vision text well enough?
        Pattern REGEX_ALPHANUMERIC = Pattern.compile(StringUtils.defaultString(regex, CAT_NO_MOST_COMMON));
        Matcher matchedText = REGEX_ALPHANUMERIC.matcher(rawVisionText);
        List<String> matches = new ArrayList<>();
        while(matchedText.find()){
            matches.add(matchedText.group());
        }

        return matches;
    }

    public static List<Album> discogsSearchAccuracyValidator(List<Album> results, DiscogsSearchAlbumRequest albumRequest, List<DiscogsSearchAlbumRequest> originals) {

        // Filter out duplicates and nulls
        List<Album> resultsWithoutOutDuplicateTitles = results.stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Album::getTitle))
                .collect(Collectors.toList());
        // TODO: Unit test this can't throw out of bounds
        // If only 1 result and catNos are exact match, this is our guy. Should I compare anything else?
        if(resultsWithoutOutDuplicateTitles.size() == 1 &&
                (resultsWithoutOutDuplicateTitles.get(0).getCatno().equalsIgnoreCase(albumRequest.getCatNo()) ||
                        resultsWithoutOutDuplicateTitles.get(0).getCatno().equalsIgnoreCase(albumRequest.getTitle()))) {
            return resultsWithoutOutDuplicateTitles;
        }

        // todo: Filter out null catNos when doing Title comparison
        // todo: Is there an album/artist field
        List<Album> titleFilteredAlbums;
        // try searching originals.title vs
        titleFilteredAlbums = originals.stream()
        .flatMap(o -> resultsWithoutOutDuplicateTitles.stream()
        .filter(r -> getStringSimilarity(o.getTitle().toLowerCase(Locale.ROOT), r.getTitle().toLowerCase(Locale.ROOT)) > 50D))
        .collect(Collectors.toList());
        if(titleFilteredAlbums.size() == 0) {
            Album album =  Album.builder()
                    .catno(albumRequest.getCatNo())
                    .title(albumRequest.getTitle())
                    .foundByCatNo(false)
                    .build();
            return List.of(album);
        }

        List<Album> resultsMatchingCatNo = resultsWithoutOutDuplicateTitles.stream()
                .filter(album -> album.getCatno().equalsIgnoreCase(albumRequest.getCatNo()))
                .collect(Collectors.toList());

        return titleFilteredAlbums;
    }

    // Vision can return album info in separate strings, so we compare the titles to see if they match and of the album results

    private static Double getStringSimilarity(String str, String str2) {
        SorensenDice sorensenDice = new SorensenDice();
        Double percentage = sorensenDice.similarity(str, str2);
        return percentage * 100;
    }

    private static List<String> getTextForIndividualAlbums(String visionResponse) {
        String[] textForIndividualAlbums = visionResponse.split("\n");

        return Arrays.stream(textForIndividualAlbums).toList();
    }

    /***
     *
     * @param keyExtractor functional interface that accepts a generic, performs the function (apply method) and returns a generic
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>(); // Map to hold values
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; //
    }
}
