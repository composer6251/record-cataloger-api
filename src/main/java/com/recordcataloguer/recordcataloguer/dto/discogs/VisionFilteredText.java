package com.recordcataloguer.recordcataloguer.dto.discogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisionFilteredText {

    private String catNo;
    private String title;
    private String confidenceLevel;
}
