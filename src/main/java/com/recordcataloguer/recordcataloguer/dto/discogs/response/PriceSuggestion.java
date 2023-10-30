package com.recordcataloguer.recordcataloguer.dto.discogs.response;

import lombok.Data;

@Data
public class PriceSuggestion {
    private String currency;
    private double value;
}