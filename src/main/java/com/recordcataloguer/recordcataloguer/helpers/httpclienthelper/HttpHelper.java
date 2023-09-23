package com.recordcataloguer.recordcataloguer.helpers.httpclienthelper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

@Slf4j
public class HttpHelper {

    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public static HttpRequest generateRequest(String uri, String authorization) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Authorization", authorization)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            return request;
        }
        catch (URISyntaxException exception) {
            log.error("Error parsing URI while building HttpRequest for URI {}", exception.getMessage());
        }
        return HttpRequest.newBuilder().build();
    }

    // Send request
    public static HttpResponse sendRequest(HttpRequest request) {

        HttpResponse response = null;
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }
}
