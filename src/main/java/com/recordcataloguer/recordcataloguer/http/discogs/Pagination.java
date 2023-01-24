package com.recordcataloguer.recordcataloguer.http.discogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class Pagination{
    @JsonProperty("page")
    private int page;
    @JsonProperty("pages")
    private int pages;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("items")
    private int items;
    @JsonProperty("urls")
    private Map<String, String> urls;
}