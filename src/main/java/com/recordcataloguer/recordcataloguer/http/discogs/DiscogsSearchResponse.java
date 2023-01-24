package com.recordcataloguer.recordcataloguer.http.discogs;

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
    List<Result> results;

    public List<DiscogsResultDTO> buildDtoFromDiscogsSearchResponse(List<Result> results){
        List<DiscogsResultDTO> discogsResultDTOList = new ArrayList<>();
        for (Result result : results) {
            discogsResultDTOList.add(DiscogsResultDTO.builder()
                    .resultsFromCatNo(results.size())
                    .catno(result.getCatno())
                    .title(result.getTitle())
                    .barcode(result.getBarcode())
                    .coverImage(result.getCoverImage())
                    .wantedBy((double) result.getCommunity().getHave())
                    .wantedBy((double) result.getCommunity().getHave())
                    .genre(result.getGenre())
                    .style(result.getStyle())
                    .id(result.getId())
                    .type(result.getType())
                    .inMyOwnedList(result.getCommunity().getHave())
                    .inMyWantList(result.getCommunity().getWant())
                    .masterId(result.getMasterId())
                    .masterUrl(result.getMasterUrl())
                    .uri(result.getUri())
                    .thumb(result.getThumb())
                    .resourceUrl(result.getResourceUrl())
                    .formatQuantity(result.getFormatQuantity())
                    .country(result.getCountry())
                    .format(result.getFormat())
                    .label(result.getLabel())
                    .type(result.getType())
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
