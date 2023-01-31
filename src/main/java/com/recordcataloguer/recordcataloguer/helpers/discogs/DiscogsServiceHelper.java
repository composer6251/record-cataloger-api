package com.recordcataloguer.recordcataloguer.helpers.discogs;

import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;
import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import info.debatty.java.stringsimilarity.SorensenDice;

@Slf4j
public class DiscogsServiceHelper {

    public static List<Album> discogsSearchAccuracyValidator(List<Album> results, DiscogsSearchAlbumRequest albumRequest, List<DiscogsSearchAlbumRequest> originals) {

        // Filter out duplicates and nulls
        List<Album> resultsWithoutOutDuplicateTitles = results.stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Album::getTitle))
                .collect(Collectors.toList());
        // TODO: Unit test this can't throw out of bounds
        // If only 1 result and catNos are exact match, this is our guy. Should I compare anything else?
        // TODO: Compare titles. If they are very similiar (and maybe format.contains('reissue')), should probably return both since the price will be different. And indicate in the UI that the album is reissue/original
        if(resultsWithoutOutDuplicateTitles.size() == 1 &&
                (resultsWithoutOutDuplicateTitles.get(0).getCatno().equalsIgnoreCase(albumRequest.getCatNo()) ||
                        resultsWithoutOutDuplicateTitles.get(0).getCatno().equalsIgnoreCase(albumRequest.getTitle()))) {
            return resultsWithoutOutDuplicateTitles;
        }
        // Check distinct titles against individualAlbumList titles
        // Check if the percentage of the title similarity. 50 is baseline test which happens often when Str1 = Artist and Str2 = Artist + Album
//        List<Album> resultsAfterFilteringByTitle = resultsWithoutOutDuplicateTitles.stream()
//                .filter(r -> getStringSimilarity(r.getTitle().toLowerCase(Locale.ROOT), albumRequest.getTitle().toLowerCase(Locale.ROOT)) > 50D)
//                .collect(Collectors.toList());

        // todo: Filter out null catNos when doing Title comparison
        // todo: Is there an album/artist field
        List<Album> titleFilteredAlbums;
        if(resultsWithoutOutDuplicateTitles.size() == 0) {
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
        }

        List<Album> resultsMatchingCatNo = resultsWithoutOutDuplicateTitles.stream()
                .filter(album -> album.getCatno().equalsIgnoreCase(albumRequest.getCatNo()))
                .collect(Collectors.toList());

        return resultsMatchingCatNo;
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

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>(); // Map to hold values
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; //
    }
}
