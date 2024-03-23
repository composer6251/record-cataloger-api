package com.recordcataloguer.recordcataloguer.dto.discogs.response.marketplaceapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsResultDTO;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Listing;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Pagination;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DiscogsUserInventoryResponse {

    @JsonProperty("Pagination")
    Pagination pagination;

    @JsonProperty("listings")
    List<Listing> listings;

    public List<DiscogsResultDTO> buildDtoFromDiscogsSearchResponse(List<Album> albums){
        List<DiscogsResultDTO> discogsResultDTOList = new ArrayList<>();
        for (Album album : albums) {
            discogsResultDTOList.add(DiscogsResultDTO.builder()
                    .resultsFromCatNo(albums.size())
                    .catNo(album.getCatno())
                    .title(album.getTitle())
                    .barcode(album.getBarcode())
                    .coverImage(album.getCoverImage())
                    .wantedBy((double) album.getCommunity().getHave())
                    .wantedBy((double) album.getCommunity().getHave())
                    .genre(album.getGenre())
                    .style(album.getStyle())
                    .id(album.getReleaseId())
                    .type(album.getType())
                    .inMyOwnedList(album.getCommunity().getHave())
                    .inMyWantList(album.getCommunity().getWant())
                    .masterId(album.getMasterId())
                    .masterUrl(album.getMasterUrl())
                    .uri(album.getUri())
                    .thumb(album.getThumb())
                    .resourceUrl(album.getResourceUrl())
                    .formatQuantity(album.getFormatQuantity())
                    .country(album.getCountry())
                    .format(album.getFormat())
                    .label(album.getLabel())
                    .type(album.getType())
                    .build());
        }

        return discogsResultDTOList;
    }
}
