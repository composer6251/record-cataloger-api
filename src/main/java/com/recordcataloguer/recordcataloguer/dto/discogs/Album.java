package com.recordcataloguer.recordcataloguer.dto.discogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
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
    private boolean foundByCatNo = false;

    /**THIS IS A COPY CONSTRUCTOR FOR USE WHEN NEEDING STREAM TO RETURN OBJECTS WITH DIFFERENT VALUES THAN LOCAL PARAM**/
    public Album(Album albumToCopy) {
        this.title = albumToCopy.title;
        this.country = albumToCopy.country;
        this.genre = albumToCopy.genre;
        this.format = albumToCopy.format;
        this.style = albumToCopy.style;
        this.id = albumToCopy.id;
        this.label = albumToCopy.label;
        this.type = albumToCopy.type;
        this.barcode = albumToCopy.barcode;
        this.masterId = albumToCopy.masterId;
        this.masterUrl = albumToCopy.masterUrl;
        this.uri = albumToCopy.uri;
        this.catno = albumToCopy.catno;
        this.thumb = albumToCopy.thumb;
        this.coverImage = albumToCopy.coverImage;
        this.resourceUrl = albumToCopy.resourceUrl;
        this.community = albumToCopy.community;
        this.formatQuantity = albumToCopy.formatQuantity;
        this.formats = albumToCopy.formats;
        this.encodedThumb = albumToCopy.encodedThumb;
    }
}
