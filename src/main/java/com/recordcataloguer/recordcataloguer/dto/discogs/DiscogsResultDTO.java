package com.recordcataloguer.recordcataloguer.dto.discogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscogsResultDTO {

        private int resultsFromCatNo;
        private String country;
        private ArrayList<String> genre;
        private ArrayList<String> format;
        private ArrayList<Object> style;
        private int id;
        private ArrayList<String> label;
        private String type;
        private ArrayList<String> barcode;
        private int inMyWantList;
        private int inMyOwnedList;
        private int masterId;
        private String masterUrl;
        private String uri;
        private String catno;
        private String title;
        private String thumb;
        private String coverImage;
        private String resourceUrl;
        private Double wantedBy;
        private Double ownedBy;
        private Map<String, Integer> community;
        private int formatQuantity;
        private ArrayList<Object> formats;

    }

//        private int totalResponses;
//        private String country;
//        private ArrayList<String> genre;
//        private ArrayList<String> format;
//        private ArrayList<Object> style;
//        private int id;
//        private ArrayList<String> label;
//        private String type;
//        private ArrayList<String> barcode;
//        //        @JsonProperty("user_data")
//        private Map<String, Boolean> userData;
//        //        @JsonProperty("master_id")
//        private int masterId;
//        //        @JsonProperty("master_url")
//        private String masterUrl;
//        private String uri;
//        private String catno;
//        private String title;
//        private String thumb;
//        //        @JsonProperty("cover_image")
//        private String coverImage;
//        //        @JsonProperty("resource_url")
//        private String resourceUrl;
//        private Map<String, Integer> community;
//        //        @JsonProperty("format_quantity")
//        private int formatQuantity;
//        private ArrayList<Format> formats;

