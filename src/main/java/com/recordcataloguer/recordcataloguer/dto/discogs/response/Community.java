package com.recordcataloguer.recordcataloguer.dto.discogs.response;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
// @Embeddable Tells Spring that this is a child object of a parent @Entity
@Embeddable
public class Community{
    private int want;
    private int have;
}