#Application designed originally to be a means of cataloguing record collections based upon the record catalogue number (usually on the side of the album cover)
##It takes an image passed into it and extracts the text from it
##Then it uses regexs to determine which text is the catalogue number
##Possible Record Catalogue Number formats are
##Alphanumeric-Alphanumeric
##Alphanumeric Alphanumeric
##Alphanumeric-Alphanumeric-Alphanumeric
##Alphanumeric Alphanumeric Alphanumeric
##TODO: ARE THERE MORE?

#*******Connecting to eBay API endpoints

###Browse API
###Endpoints:
####NOTE: Response results maybe be limited within the eBay development sandbox. May need to use production URL(https://api.ebay.com....)
###search
url: https://api.sandbox.ebay.com/buy/browse/v1/item_summary/search?q=Cat%20Stevens

####query params: 
q=<text> //Keyword/s to search

####headers:
Authorization: Bearer `<OAuthToken>` // Must be OAuthToken. \
Accept: application/json



##search_by_image

## To solve "library not found " error, add to the java.library.path env var (which is were JVM looks for native libraries)
### Go to run configurations, add this to VM options
-Djava.library.path="/Users/david/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:/opt/homebrew/Cellar/leptonica/1.82.0_1/lib"
-Djava.library.path="/Users/david/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:/opt/homebrew/Cellar/tesseract/5.2.0/lib"
The above solution fixed the first library not found but not the second.
Where is the jnilept bin?
Find it and update library path
Or
Try tess4j?

#Cloud Vision APIs

#DISCOGS APIs

## Database API
### Search Endpoint
#### Example: https://api.discogs.com/database/search?release_title=nevermind&artist=nirvana&per_page=3&page=1


#Flow to get album info from 3rd party APIs
### Send to Discogs with catno
    #### Filter out non US(UK?)
    #### What to do with multiples?
    #### Use barcode to look up correct one

###Or
    #### Filter out non US
    #### Filter duplicate artists/title
    #### Can I find a dependable barcode?

###Or
    #### Just grab first result object and present to user. Is this dependable? Need to test
    #### Filter duplicate artists/title
    #### Present options to user and let them select or manual lookup



###Setting up Spring GCP for image analysis from Google Vision API
####Link to spring-cloud-gcp vision sample how to: repo: https://github.com/GoogleCloudPlatform/spring-cloud-gcp/tree/main/spring-cloud-gcp-samples/spring-cloud-gcp-vision-api-sample
Add POM dependencies

####Add authentication
go to: https://cloud.google.com/iam/docs/using-workload-identity-federation#github-actions_1
click on Enable the APIs button
Enable the APIs for the service account you have created
When you are done, go to https://console.cloud.google.com
This should take you to the Google cloud console and have your project number/project id needed for next steps
##TODO: FINISH USING DEFAULT AUTH SETUP. APP KEY IS UNSECURE


####AUTH WITH APP KEY. THIS SHOULD BE CHANGED!!!!
Check directory Users/<user>/ for a hidden json file such as "....cloud-vision"
Add spring.cloud.gcp.credentials.location=file:/path/to/file.json to application properties



### CATALOG NUMBERS

#### BY COUNTRY
The "R" often stands for "Recordds" and the preceeding letters are the record label name
Does the "V" also stand for "Vinyl"
Are they always capital letters? The catnos?
Start with "ELV002" format since it's the least likely to be a false positive
US: ELV002 CSS 873 BBBR-115 //  RARE *** B0007580-02 
UK:
FRANCE: 884 815-7 | 
Italy: CDL 337