package com.recordcataloguer.recordcataloguer.http.discogs;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
public class DiscogsSearchResponse {

    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */

    /***Add to class
     *         public Pagination pagination;
     *         public ArrayList<Result> results;
     *
     * And pull classes OUT of public
     *
     * Make them private
     */
    public class Community{
        public int want;
        public int have;
    }

    public class Format{
        public String name;
        public String qty;
        public String text;
        public ArrayList<String> descriptions;
    }

    public class Pagination{
        public int page;
        public int pages;
        public int per_page;
        public int items;
        public Urls urls;
    }

    public class Result{
        public String country;
        public ArrayList<String> genre;
        public ArrayList<String> format;
        public ArrayList<Object> style;
        public int id;
        public ArrayList<String> label;
        public String type;
        public ArrayList<String> barcode;
        public UserData user_data;
        public int master_id;
        public String master_url;
        public String uri;
        public String catno;
        public String title;
        public String thumb;
        public String cover_image;
        public String resource_url;
        public Community community;
        public int format_quantity;
        public ArrayList<Format> formats;
    }

    public class Root{
        public Pagination pagination;
        public ArrayList<Result> results;
    }

    public class Urls{
    }

    public class UserData{
        public boolean in_wantlist;
        public boolean in_collection;
    }


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