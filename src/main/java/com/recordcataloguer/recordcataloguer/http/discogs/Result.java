package com.recordcataloguer.recordcataloguer.http.discogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
public class Result{
    @JsonProperty("title")
    private String title;
    @JsonProperty("country")
    private String country;
    @JsonProperty("genre")
    private ArrayList<String> genre;
    @JsonProperty("format")
    private ArrayList<String> format;
    @JsonProperty("style")
    private ArrayList<Object> style;
    @JsonProperty("id")
    private int id;
    @JsonProperty("label")
    private ArrayList<String> label;
    @JsonProperty("type")
    private String type;
    @JsonProperty("barcode")
    private ArrayList<String> barcode;
    @JsonProperty("master_id")
    private int masterId;
    @JsonProperty("master_url")
    private String masterUrl;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("catno")
    private String catno;
    @JsonProperty("thumb")
    private String thumb;
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
    private String catalogNumberForLookup;
    private double albumMintPlusValue;
    private double albumGoodValue;

    /**THIS IS A COPY CONSTRUCTOR FOR USE WHEN NEEDING STREAM TO RETURN OBJECTS WITH DIFFERENT VALUES THAN LOCAL PARAM**/
    public Result(Result resultToCopy) {
        this.title = resultToCopy.title;
        this.country = resultToCopy.country;
        this.genre = resultToCopy.genre;
        this.format = resultToCopy.format;
        this.style = resultToCopy.style;
        this.id = resultToCopy.id;
        this.label = resultToCopy.label;
        this.type = resultToCopy.type;
        this.barcode = resultToCopy.barcode;
        this.masterId = resultToCopy.masterId;
        this.masterUrl = resultToCopy.masterUrl;
        this.uri = resultToCopy.uri;
        this.catno = resultToCopy.catno;
        this.thumb = resultToCopy.thumb;
        this.coverImage = resultToCopy.coverImage;
        this.resourceUrl = resultToCopy.resourceUrl;
        this.community = resultToCopy.community;
        this.formatQuantity = resultToCopy.formatQuantity;
        this.formats = resultToCopy.formats;
        this.encodedThumb = resultToCopy.encodedThumb;
    }
}
