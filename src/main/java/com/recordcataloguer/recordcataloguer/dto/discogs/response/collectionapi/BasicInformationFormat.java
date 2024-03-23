package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@AllArgsConstructor
@Builder
@Data
public class BasicInformationFormat {

    @JsonProperty("descriptions")
    private ArrayList descriptions;

    @JsonProperty("name")
    private String name;

    @JsonProperty("qty")
    private String qty;
}
