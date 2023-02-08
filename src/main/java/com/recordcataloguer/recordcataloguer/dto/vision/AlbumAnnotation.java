package com.recordcataloguer.recordcataloguer.dto.vision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumAnnotation {

    private int index;
    private String description;
    private int xVerticesSum;
    private int yVerticesSum;

    private int firstXVert;
    private int secondXVert;
    private int thirdXVert;
    private int fourthXVert;

//    private int firstYVert;

}
