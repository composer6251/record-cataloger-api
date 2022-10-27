package com.recordcataloguer.recordcataloguer.helpers.encode;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/***
 * This class encodes/decodes images in order to send them in http payloads
 */
@Slf4j
public class ImageEncodingHelper {

//    private static final String filePath = "/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/20221017_163141.jpg";

    // TODO: Can we use the record number by extracting text from image?
    // Is the eBay image endpoint useless?
    // Are record numbers predictable in length/format?

    public ImageEncodingHelper() throws IOException {

    }

    public static final String encodeImage(String inputFileName, String outputFileName) throws IOException {
        log.info("Encoding image at location {}", inputFileName);

        // Default outputFileName if none
        outputFileName = outputFileName.isEmpty() ? "" : outputFileName;

        String encodedString = "";
        // Determine if valid base64 A-Za-z0-9+/
        //get file content in binary
        byte[] fileContent = FileUtils.readFileToByteArray(new File(inputFileName));
//        log.info("binary image content {}", fileContent);

        encodedString = Base64.getEncoder().encodeToString(fileContent);
        FileUtils.write(new File("/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/testString"), encodedString);
//        log.info("binary encoded as string {}", encodedString);
        //encode binary to string

        // Write decoded
        // Todo: Correct file being used?
        return encodedString;

    }

    public static final String decodeImage(String encodedString, String outputFileName) throws IOException {
        outputFileName = outputFileName.isEmpty() ? "" : outputFileName;
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        FileUtils.writeByteArrayToFile(new File(outputFileName), decodedBytes);


        return encodedString;
    }

}
