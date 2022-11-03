package com.recordcataloguer.recordcataloguer.helpers.http;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpHelper {
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
//            .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
            .authenticator(Authenticator.getDefault())
            .build();

    public static HttpRequest generateRequest(String baseUrl) throws URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl))
                .build();

        return request;
    }

    // Send request
    public static HttpResponse sendRequest(HttpRequest request, HttpResponse.BodyHandler<Object> responseBodyHandler)
            throws IOException, InterruptedException {

        HttpResponse response = client.send(request, responseBodyHandler);

        return response;
    }
    // Generate request w/body?...etc?
}
