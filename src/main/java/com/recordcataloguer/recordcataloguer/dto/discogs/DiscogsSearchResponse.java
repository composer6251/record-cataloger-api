package com.recordcataloguer.recordcataloguer.dto.discogs;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscogsSearchResponse {

    @JsonProperty("Pagination")
    Pagination pagination;

    @JsonProperty("results")
    List<Album> albums;

    public List<DiscogsResultDTO> buildDtoFromDiscogsSearchResponse(List<Album> albums){
        List<DiscogsResultDTO> discogsResultDTOList = new ArrayList<>();
        for (Album album : albums) {
            discogsResultDTOList.add(DiscogsResultDTO.builder()
                    .resultsFromCatNo(albums.size())
                    .catno(album.getCatno())
                    .title(album.getTitle())
                    .barcode(album.getBarcode())
                    .coverImage(album.getCoverImage())
                    .wantedBy((double) album.getCommunity().getHave())
                    .wantedBy((double) album.getCommunity().getHave())
                    .genre(album.getGenre())
                    .style(album.getStyle())
                    .id(album.getId())
                    .type(album.getType())
                    .inMyOwnedList(album.getCommunity().getHave())
                    .inMyWantList(album.getCommunity().getWant())
                    .masterId(album.getMasterId())
                    .masterUrl(album.getMasterUrl())
                    .uri(album.getUri())
                    .thumb(album.getThumb())
                    .resourceUrl(album.getResourceUrl())
                    .formatQuantity(album.getFormatQuantity())
                    .country(album.getCountry())
                    .format(album.getFormat())
                    .label(album.getLabel())
                    .type(album.getType())
                    .build());
        }

        return discogsResultDTOList;
    }


//    @Data
//    class Community{
//        private int want;
//        private int have;
//    }
//    @Data
//    class Format{
//        private String name;
//        private String qty;
//        private String text;
//        private ArrayList<String> descriptions;
//    }
//    @Data
//    class Pagination{
//        private int page;
//        private int pages;
//        private int perPage;
//        private int items;
//    }
//    @Data
//    class Result{
//        private String country;
//        private ArrayList<String> genre;
//        private ArrayList<String> format;
//        private ArrayList<Object> style;
//        private int id;
//        private ArrayList<String> label;
//        private String type;
//        private ArrayList<String> barcode;
//        private int masterId;
//        private String masterUrl;
//        private String uri;
//        private String catno;
//        public String title;
//        private String thumb;
//        private String coverImage;
//        private String resourceUrl;
//        private Community community;
//        private int formatQuantity;
//        private ArrayList<String> formats;
//
//    }
}
