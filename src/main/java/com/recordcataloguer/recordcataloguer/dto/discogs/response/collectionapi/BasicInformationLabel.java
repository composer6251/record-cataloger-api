package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class BasicInformationLabel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("entity_type")
    private String entityType;

    @JsonProperty("catno")
    private String catno;

    @JsonProperty("resource_url")
    private String resourceUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("entity_type_name")
    private String entityTypeName;
}
