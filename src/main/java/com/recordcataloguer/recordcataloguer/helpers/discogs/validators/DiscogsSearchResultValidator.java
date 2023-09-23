package com.recordcataloguer.recordcataloguer.helpers.discogs.validators;

import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsSearchAlbumRequest;
import info.debatty.java.stringsimilarity.SorensenDice;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class DiscogsSearchResultValidator {

    public static List<Album> validateSearchResultsByRequestAndResults(List<Album> results, DiscogsSearchAlbumRequest albumRequest, List<DiscogsSearchAlbumRequest> originals) {

        // Filter out duplicates and nulls
        List<Album> resultsWithoutOutDuplicateTitles = filterOutResponseDuplicates(results);
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
        // try searching originals.title vs returned
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

        return titleFilteredAlbums;
    }

    public static List<Album> filterOutResponseDuplicates(List<Album> searchResults) {
        return searchResults.stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(Album::getTitle))
                .filter(a -> (a.getReleaseId() != null || a.getReleaseId().isEmpty()))
                .collect(Collectors.toList());
    }

    // TODO: Fix or remove. Albums with the same catNo CAN BE DIFFERENT ALBUMS.
    public static List<Album> getSingleExactCatalogNumberMatch(List<Album> searchResults, String scannedCatalogNumber) {
        return searchResults.stream()
                .filter(Objects::nonNull)
                .filter(r -> r.getCatno().equalsIgnoreCase(scannedCatalogNumber))
                .collect(Collectors.toList());
    }

    public static List<Album> filterOutByAnnotationDescriptionExact(List<Album> searchResults, List<String> descriptions) {
        List<Album> exactResults = searchResults.stream()
                .filter(s -> s.getSearchQuery().contains(s.getTitle()))
                .collect(Collectors.toList());

        return exactResults;
    }
    public static List<Album> filterOutByAnnotationDescriptionSimilar(List<Album> searchResults, List<String> descriptions) {
        Double similarPercent = 0D;
        List<Album> similarAlbums = new ArrayList<>();
        for (Album album: searchResults
             ) {
            for (String description:
                 descriptions) {
                similarPercent = getStringSimilarity(album.getSearchQuery(), description);
                log.info("similarity for {} and {}: {}", album.getSearchQuery(), description, similarPercent);
            }
            if(similarPercent > 40D) similarAlbums.add(album);
        }
        return similarAlbums;
    }

    /***
     *
     * @param keyExtractor functional interface that accepts a generic, performs the function (apply method) and returns a generic
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>(); // Map to hold values
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; //
    }


    // Vision can return album info in separate strings, so we compare the titles to see if they match and of the album results

    private static Double getStringSimilarity(String str, String str2) {
        SorensenDice sorensenDice = new SorensenDice();
        Double percentage = sorensenDice.similarity(str, str2);
        return percentage * 100;
    }


}
