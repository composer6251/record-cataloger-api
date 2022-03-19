package com.recordcataloguer.recordcataloguer.client;

/// *
/// This holds immutable strings of the urls used in the application
public class EbayUrls {

final static String EBAY_SANDBOX_BASE_URL = "api.sandbox.ebay.com";
final static String  ebayProductionBaseUri = "api.ebay.com";

/*****AUTHORIZATION******/
// These API urls are to get the OAuth tokens required to talk to Ebays inventory APIs.
//https://developer.ebay.com/api-docs/static/oauth-client-credentials-grant.html
final static String SANDBOX_AUTHORIZATION_API_PATH = "/identity/v1/oauth2/token";
final static String  EBAY_PRODUCTION_AUTHORIZATION_API_PATH = "/identity/v1/oauth2/token";

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
final static String  catalogApiSearchUrl = "/commerce/catalog/v1_beta/product_summary/search?";
final static String  catalogApiGetProductUrl = "/commerce/catalog/v1_beta/product/";

/// ACCESS KEYS
final static String  ebaySandboxApiClientId = "DavidFen-recordca-SBX-96ae2d65c-d44e5717";
final static String  ebaySandboxApiClientSecret = "SBX-6ae2d65c2f23-1ab7-45c9-8ac8-6048";
final static String  unencodedClientIdSecret = "DavidFen-recordca-SBX-96ae2d65c-d44e5717:SBX-6ae2d65c2f23-1ab7-45c9-8ac8-6048";
final static String  encodedClientIdSecret = "RGF2aWRGZW4tcmVjb3JkY2EtU0JYLTk2YWUyZDY1Yy1kNDRlNTcxNzpTQlgtNmFlMmQ2NWMyZjIzLTFhYjctNDVjOS04YWM4LTYwNDg=";

}
