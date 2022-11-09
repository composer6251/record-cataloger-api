package com.recordcataloguer.recordcataloguer.http.discogs;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Result{
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
    public String title;
    private String thumb;
    private String coverImage;
    private String resourceUrl;
    private Community community;
    private int formatQuantity;
    private ArrayList<String> formats;
}
