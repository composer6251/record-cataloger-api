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