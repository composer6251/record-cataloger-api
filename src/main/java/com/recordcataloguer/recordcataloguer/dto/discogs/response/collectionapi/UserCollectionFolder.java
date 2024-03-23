package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserCollectionFolder {
    @JsonProperty("id")
    private String id;
    @JsonProperty("count")
    private String count;
    @JsonProperty("name")
    private String name;
    @JsonProperty("resource_url")
    private String resourceUrl;

    /**THIS IS A COPY CONSTRUCTOR FOR USE WHEN NEEDING STREAM TO RETURN OBJECTS WITH DIFFERENT VALUES THAN LOCAL PARAM**/
    public UserCollectionFolder(UserCollectionFolder albumToCopy) {
        this.id = albumToCopy.id;
        this.count = albumToCopy.count;
        this.name = albumToCopy.name;
        this.resourceUrl = albumToCopy.resourceUrl;
    }
}
