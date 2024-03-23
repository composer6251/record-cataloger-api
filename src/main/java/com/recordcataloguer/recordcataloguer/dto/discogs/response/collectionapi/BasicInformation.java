package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class BasicInformation {

    @JsonProperty("labels")
    private List<BasicInformationLabel> labels;

    @JsonProperty("formats")
    private List<BasicInformationFormat> formats;

    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artists")
    private ArrayList<Object> artists;

    @JsonProperty("resource_url")
    private String resourceUrl;

    @JsonProperty("year")
    private String year;

    @JsonProperty("id")
    private String id;


}
