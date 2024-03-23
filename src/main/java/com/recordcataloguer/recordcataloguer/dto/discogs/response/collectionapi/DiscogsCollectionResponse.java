package com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiscogsCollectionResponse {
    @JsonProperty("folders")
    private List<UserCollectionFolder> folders;
}
