package com.recordcataloguer.recordcataloguer.helpers.image.encode;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/***
 * This class encodes/decodes images in order to send them in http payloads
 */
@Slf4j
public class ImageEncodingHelper {

    private ImageEncodingHelper(){}

    public static String encodeImage(String inputFileName, String outputFileName) throws IOException {
        log.info("Encoding image at location {}", inputFileName);

        String encodedString = "";

        //get file content in binary
        byte[] fileContent = FileUtils.readFileToByteArray(new File(inputFileName));

        //encode binary to string
        encodedString = Base64.getEncoder().encodeToString(fileContent);

        // Write the encoded string to a file
        FileUtils.write(new File("/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/images/testString"), encodedString);

        return encodedString;
    }

    public static final String decodeImage(String encodedString, String outputFileName) throws IOException {
        if(outputFileName.isEmpty()) throw new IOException("Output filename cannot be empty for encoding an image");if(outputFileName.isEmpty()) throw new IOException("File name cannot be empty for encoding an image");

        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);

        FileUtils.writeByteArrayToFile(new File(outputFileName), decodedBytes);

        return encodedString;
    }

    /******TODO: Remove if unnecessary********/
    public static byte[] getEncodedImageFromDiscogs(String url) {

        byte[] encodedImage = null;

        try {
            InputStream in = ImageEncodingHelper.class.getResourceAsStream(url);
            encodedImage = IOUtils.toByteArray(in);
        } catch (IOException e) {
            log.error("Unable to encode image for url " + url + " \n with error"+ e.getMessage());
            e.printStackTrace();
        }
        return encodedImage;
    }
}
