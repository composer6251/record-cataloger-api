package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Pagination;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.Release;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserCollectionByFolderResponse {
    @JsonProperty("Pagination")
    Pagination pagination;

    @JsonProperty("releases")
    private List<Release> releases;
}
