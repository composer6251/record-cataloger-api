package com.recordcataloguer.recordcataloguer.dto.discogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PriceSuggestionsResponse {

    @JsonProperty("Mint (M)")
    private String mint;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("value")
    private String value;


}
