package com.recordcataloguer.recordcataloguer.http.discogs;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscogsSearchResponse {

    Pagination pagination;

    List<Result> results;
}

@Data
 class Community{
    private int want;
    private int have;
}
@Data
class Format{
    private String name;
    private String qty;
    private String text;
    private ArrayList<String> descriptions;
}
@Data
class Pagination{
    private int page;
    private int pages;
    private int per_page;
    private int items;
}
@Data
class Result{
    private String country;
    private ArrayList<String> genre;
    private ArrayList<String> format;
    private ArrayList<Object> style;
    private int id;
    private ArrayList<String> label;
    private String type;
    private ArrayList<String> barcode;
    private int master_id;
    private String master_url;
    private String uri;
    private String catno;
    private String title;
    private String thumb;
    private String cover_image;
    private String resource_url;
    private Community community;
    private int format_quantity;
    private ArrayList<Format> formats;

}

/***Sample Response
 *
 * {
 *     "pagination": {
 *         "page": 1,
 *         "pages": 1,
 *         "per_page": 50,
 *         "items": 1,
 *         "urls": {}
 *     },
 *     "results": [
 *         {
 *             "country": "US",
 *             "genre": [
 *                 "Rock"
 *             ],
 *             "format": [
 *                 "Vinyl",
 *                 "LP",
 *                 "Album",
 *                 "Reissue"
 *             ],
 *             "style": [],
 *             "id": 23855549,
 *             "label": [
 *                 "Capricorn Records",
 *                 "Capricorn Records Inc.",
 *                 "Record Plant, Sausalito",
 *                 "Columbia Records Pressing Plant, Pitman"
 *             ],
 *             "type": "release",
 *             "barcode": [
 *                 "CPN 0151 A",
 *                 "CPN 0151 B",
 *                 "P CPN-0151A-1B I A",
 *                 "P CPN-0151B-1B I B"
 *             ],
 *             "user_data": {
 *                 "in_wantlist": false,
 *                 "in_collection": false
 *             },
 *             "master_id": 307062,
 *             "master_url": "https://api.discogs.com/masters/307062",
 *             "uri": "/release/23855549-Elvin-Bishop-Juke-Joint-Jump",
 *             "catno": "CPN 0151",
 *             "title": "Elvin Bishop - Juke Joint Jump",
 *             "thumb": "https://i.discogs.com/lj2WNl_eav23zP6zqXv1tDRhegNduUG2ol6fggpMEFo/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTIzODU1/NTQ5LTE2NTc1Njkz/NDYtNDU4My5qcGVn.jpeg",
 *             "cover_image": "https://i.discogs.com/2LK38dFbanNA561erKl5Y-wT0GMREDkARHdRbODApEA/rs:fit/g:sm/q:90/h:430/w:450/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTIzODU1/NTQ5LTE2NTc1Njkz/NDYtNDU4My5qcGVn.jpeg",
 *             "resource_url": "https://api.discogs.com/releases/23855549",
 *             "community": {
 *                 "want": 1,
 *                 "have": 1
 *             },
 *             "format_quantity": 1,
 *             "formats": [
 *                 {
 *                     "name": "Vinyl",
 *                     "qty": "1",
 *                     "text": "Pitman Pressing",
 *                     "descriptions": [
 *                         "LP",
 *                         "Album",
 *                         "Reissue"
 *                     ]
 *                 }
 *             ]
 *         }
 *     ]
 * }
 *
 * ***/