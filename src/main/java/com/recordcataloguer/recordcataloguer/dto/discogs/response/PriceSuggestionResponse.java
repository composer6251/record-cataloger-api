package com.recordcataloguer.recordcataloguer.dto.discogs.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.PriceSuggestion;
import lombok.Data;

@Data
public class PriceSuggestionResponse {

    @JsonProperty("Mint (M)")
    PriceSuggestion mint;

    @JsonProperty("Near Mint (NM or M-)")
    PriceSuggestion nearMint;

    @JsonProperty("Very Good Plus (VG+)")
    PriceSuggestion veryGoodPlus;

    @JsonProperty("Very Good (VG)")
    PriceSuggestion veryGood;

    @JsonProperty("Good Plus (G+)")
    PriceSuggestion goodPlus;

    @JsonProperty("Good (G)")
    PriceSuggestion good;

    @JsonProperty("Fair (F)")
    PriceSuggestion fair;

    @JsonProperty("Poor (P)")
    PriceSuggestion poor;
}