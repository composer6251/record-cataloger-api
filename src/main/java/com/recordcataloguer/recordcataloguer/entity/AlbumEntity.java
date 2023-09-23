package com.recordcataloguer.recordcataloguer.entity;

import com.recordcataloguer.recordcataloguer.dto.discogs.Album;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;

/***
 * Entity class for album table.
 */

@Entity
@Table(name = "album", schema = "discogs")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "discogs_release_id")
    private String discogsReleaseId;
    @Column(name = "title")
    private String title;
    @Column(name = "country")
    private String country;
    @Column(name = "genre")
    private ArrayList<String> genre;
    @Column(name = "format")
    private ArrayList<String> format;
    @Column(name = "style")
    private ArrayList<Object> style;
    @Column(name = "label")
    private ArrayList<String> label;
    @Column(name = "type")
    private String type;
    @Column(name = "barcode")
    private ArrayList<String> barcode;
    @Column(name = "master_id")
    private int masterId;
    @Column(name = "master_url")
    private String masterUrl;
    @Column(name = "uri")
    private String uri;
    @Column(name = "catno")
    private String catno;
    @Column(name = "thumb")
    private String thumb;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "resource_url")
    private String resourceUrl;
    @Column(name = "community_want")
    private int communityWant;
    @Column(name = "community_have")
    private int communityHave;
    @Column(name = "format_quantity")
    private int formatQuantity;

    public static AlbumEntity buildAlbumEntityFromAlbum(Album album) {
        return AlbumEntity.builder()
                .catno(album.getCatno())
                .title(album.getTitle())
                .country(album.getCountry())
                .discogsReleaseId(album.getReleaseId())
                .genre(album.getGenre())
                .format(album.getFormat())
                .style(album.getStyle())
                .label(album.getLabel())
                .type(album.getType())
                .barcode(album.getBarcode())
                .masterId(album.getMasterId())
                .masterUrl(album.getMasterUrl())
                .uri(album.getUri())
                .thumb(album.getThumb())
                .coverImage(album.getCoverImage())
                .resourceUrl(album.getResourceUrl())
                .formatQuantity(album.getFormatQuantity())
                .communityWant(album.getCommunity().getWant())
                .communityHave(album.getCommunity().getHave())
                .build();
    }
}
