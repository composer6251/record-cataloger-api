package com.recordcataloguer.recordcataloguer.helpers.http;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

@Slf4j
public class HttpHelper {

    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
//            .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
            .authenticator(Authenticator.getDefault())
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
//            throw new URISyntaxException("Error parsing URI while building HttpRequest for URI ", exception.getMessage());
        }
        return HttpRequest.newBuilder().build();
    }

    // Send request
    public static HttpResponse sendRequest(HttpRequest request)
            throws IOException, InterruptedException {

        HttpResponse response = client.send(request, BodyHandlers.ofString());

        return response;
    }
    // Generate request w/body?...etc?
}
