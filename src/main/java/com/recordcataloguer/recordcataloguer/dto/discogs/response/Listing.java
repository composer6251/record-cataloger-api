package com.recordcataloguer.recordcataloguer.dto.discogs.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.Release;
import lombok.*;

import java.util.ArrayList;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Listing {
    @JsonProperty("status")
    private String status;
    @JsonProperty("price")
    private Map<String, String> price;
    @JsonProperty("weight")
    private String weight;
    @JsonProperty("genre")
    private ArrayList<String> genre;
    @JsonProperty("format")
    private ArrayList<String> format;
    @JsonProperty("style")
    private ArrayList<String> style;
    @JsonProperty("label")
    private ArrayList<String> label;
    @JsonProperty("quantity")
    private String quantity;
    @JsonProperty("barcode")
    private ArrayList<String> barcode;
    @JsonProperty("external_id")
    private int external_id;
    @JsonProperty("master_url")
    private String masterUrl;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("catalog_number")
    private String catalogNumber;
    @JsonProperty("release")
    private Release release;
    @JsonProperty("cover_image")
    private String coverImage;
    @JsonProperty("resource_url")
    private String resourceUrl;
    @JsonProperty("community")
    private Community community;
    @JsonProperty("format_quantity")
    private int formatQuantity;
    @JsonProperty("formats")
    private ArrayList<Object> formats;
    private byte[] encodedThumb;
    private String searchQuery;
    private double albumMintPlusValue;
    private double albumGoodValue;
    private boolean foundByCatNo = true;
}

