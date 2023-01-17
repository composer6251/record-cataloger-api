package com.recordcataloguer.recordcataloguer.http.discogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
public class Result{
    private String title;
    private String country;
    private ArrayList<String> genre;
    private ArrayList<String> format;
    private ArrayList<Object> style;
    private int id;
    private ArrayList<String> label;
    private String type;
    private ArrayList<String> barcode;
    private int masterId;
    private String masterUrl;
    private String uri;
    private String catno;
    private String thumb;
    private String coverImage;
    private String resourceUrl;
    private Community community;
    private int formatQuantity;
    private ArrayList<Object> formats;
    private byte[] encodedThumb;
    private String catalogNumberForLookup;

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
