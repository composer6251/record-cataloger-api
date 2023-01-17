//package com.recordcataloguer.recordcataloguer.http.discogs;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.Map;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class DiscogsSearchResult {
//
//    @JsonProperty("pagination")
//    private void mapPaginationField(Map<String, Integer> pagination) {
//        this.numberOfResultItems = pagination.get("items");
//    }
//    private Integer numberOfResultItems;
//
//    @JsonProperty("results")
//    private List<DiscogsResultDTO> results;
//
//    private void mapResultObjects(){
//        results =
//    }
//
//}