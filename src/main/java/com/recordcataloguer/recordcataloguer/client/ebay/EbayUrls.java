package com.recordcataloguer.recordcataloguer.client.ebay;

import lombok.Data;

/// *
/// This holds immutable strings of the urls used in the application
public class EbayUrls {

public final static String EBAY_SANDBOX_BASE_URL = "https://api.sandbox.ebay.com";
public final static String EBAY_PRODUCTION_BASE_URI = "api.ebay.com";

/*****AUTHORIZATION******/
// These API urls are to get the OAuth tokens required to talk to Ebays inventory APIs.
//https://developer.ebay.com/api-docs/static/oauth-client-credentials-grant.html
public final static String SANDBOX_AUTHORIZATION_API_PATH = "/identity/v1/oauth2/token";
public final static String  EBAY_PRODUCTION_AUTHORIZATION_API_PATH = "/identity/v1/oauth2/token";

/// ****PRODUCTION CATALOG API ENDPOINTS (https://developer.ebay.com/api-docs/commerce/catalog/resources/methods)*****///
/// GET https://api.ebay.com/commerce/catalog/v1_beta/product_summary/search?
// q=string&   // ex: q=cat stephens,teaforthetillerman
// gtin=string&  // UPC
// mpn=string&
// category_ids=string&
// aspect_filter=AspectFilter&
// fieldgroups=string&
// limit=string&
// offset=string
public final static String CATALOG_API_SEARCH_PATH = "/commerce/catalog/v1_beta/product_summary/search?";
public final static String CATALOG_API_GET_PRODUCT_URL = "/commerce/catalog/v1_beta/product/";

/// ACCESS KEYS
public final static String EBAY_SANDBOX_API_CLIENT_ID = "DavidFen-recordca-SBX-96ae2d65c-d44e5717";
public final static String EBAY_SANDBOX_API_CLIENT_SECRET = "SBX-6ae2d65c2f23-1ab7-45c9-8ac8-6048";
public final static String UNENCODED_CLIENT_ID_SECRET = "DavidFen-recordca-SBX-96ae2d65c-d44e5717:SBX-6ae2d65c2f23-1ab7-45c9-8ac8-6048";
public final static String ENCODED_CLIENT_ID_SECRET = "RGF2aWRGZW4tcmVjb3JkY2EtU0JYLTk2YWUyZDY1Yy1kNDRlNTcxNzpTQlgtNmFlMmQ2NWMyZjIzLTFhYjctNDVjOS04YWM4LTYwNDg=";

}
