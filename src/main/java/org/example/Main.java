package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    public static final String NASA_URL = "https://api.nasa.gov/planetary/apod?api_key=2PX0aY3gkNCPJpSldUgGbhhiKJhKWzfgD1tn23Tw";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(NASA_URL);
        CloseableHttpResponse response = httpClient.execute(request);
        Planetary planetary = mapper.readValue(response.getEntity().getContent(),
                new TypeReference<>() {
                }
        );

        String url = planetary.getUrl();

        try (InputStream inputStream = new URL(url).openStream()) {
            Files.copy(inputStream, Paths.get(url.substring(url.lastIndexOf("/") + 1)),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        response.close();
        httpClient.close();
    }
}