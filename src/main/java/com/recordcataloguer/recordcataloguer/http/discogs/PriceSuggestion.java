package com.recordcataloguer.recordcataloguer.http.discogs;

import lombok.Data;

@Data
public class PriceSuggestion {
    private String currency;
    private double value;
}