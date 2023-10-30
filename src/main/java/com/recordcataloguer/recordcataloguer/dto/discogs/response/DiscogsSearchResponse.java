package com.recordcataloguer.recordcataloguer.dto.discogs.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recordcataloguer.recordcataloguer.dto.discogs.DiscogsResultDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscogsSearchResponse {

    @JsonProperty("Pagination")
    Pagination pagination;

    @JsonProperty("results")
    List<Album> albums;

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
