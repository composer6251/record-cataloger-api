package com.recordcataloguer.recordcataloguer.dto.discogs.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscogsSearchAlbumRequest {

    private String catNo;
    private String title;
    private String originalString;
    private int confidenceLevel;
}
