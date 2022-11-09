package com.recordcataloguer.recordcataloguer.http.discogs;

import lombok.Data;

@Data
public class Pagination{
    private int page;
    private int pages;
    private int perPage;
    private int items;
}