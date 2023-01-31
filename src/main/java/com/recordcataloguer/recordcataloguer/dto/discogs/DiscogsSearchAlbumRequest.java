package com.recordcataloguer.recordcataloguer.dto.discogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscogsSearchAlbumRequest {

    private String catNo;
    private String title;
    private String originalString;
    private int confidenceLevel;
}
