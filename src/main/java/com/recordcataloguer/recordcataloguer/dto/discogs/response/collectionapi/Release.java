package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Release{

    @JsonProperty("instance_id")
    private String instanceId;
    @JsonProperty("rating")
    private String rating;
    @JsonProperty("basic_information")
    private BasicInformation basicInformation;
    @JsonProperty("folder_id")
    private String folderId;
    @JsonProperty("date_added")
    private String dateAdded;
    @JsonProperty("id")
    private String id;
}