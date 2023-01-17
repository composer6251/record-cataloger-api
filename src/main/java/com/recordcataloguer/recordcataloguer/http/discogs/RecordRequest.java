package com.recordcataloguer.recordcataloguer.http.discogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordRequest {


    @NonNull
    private String url;

}