package com.recordcataloguer.recordcataloguer.entity;

import com.recordcataloguer.recordcataloguer.dto.discogs.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;

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
//    private ArrayList<String> barcode;
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
//    @Embedded
//    private Community community;
    private int formatQuantity;
}
