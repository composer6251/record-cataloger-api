package com.recordcataloguer.recordcataloguer.helpers.discogs;

import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DiscogsServiceHelper {

    public static List<Album> discogsSearchAccuracyValidator(List<Album> results, DiscogsSearchAlbumRequest albumRequest, List<DiscogsSearchAlbumRequest> originals) {

        // Filter out duplicates and nulls
        List<Album> resultsWithoutOutDuplicateTitles = results.stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Album::getTitle))
                .collect(Collectors.toList());
        // TODO: Unit test this can't throw out of bounds
        // If only 1 result and catNos are exact match, this is our guy. Should I compare anything else?
        if(resultsWithoutOutDuplicateTitles.size() == 1 && resultsWithoutOutDuplicateTitles.get(0).getCatno().equalsIgnoreCase(albumRequest.getCatNo())) {
            return resultsWithoutOutDuplicateTitles;
        }
        // Check results against individualAlbumList

        // REQUEST BY CATNO
        List<Album> resultsMatchingCatNo = resultsWithoutOutDuplicateTitles.stream()
                .filter(album -> album.getCatno().equalsIgnoreCase(albumRequest.getCatNo()))
                .collect(Collectors.toList());
        // If only on
        // COMPARE CATNO/ARTIST/ALBUM/TITLE FROM RESULT TO DETERMINE MOST LIKELY POSITIVE-POSITIVES
        // CRITERIA:
        //
        // result.artist is like( actual %) original string
        // RESULT
        // Create confidence level based on
        // length of cat no >= 6
        // does catno sent match EXACTLY(except case) result.catno?
        // % match
    }

    private static List<String> getTextForIndividualAlbums(String visionResponse) {
        String[] textForIndividualAlbums = visionResponse.split("\n");

        return Arrays.stream(textForIndividualAlbums).toList();
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>(); // Map to hold values
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; //
    }
}
