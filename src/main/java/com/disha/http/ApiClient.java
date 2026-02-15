package com.disha.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ApiClient {
    private String baseUrl;
    private Map<String, String> defaultHeaders;

    public ApiClient() {
        this.baseUrl = "";
        this.defaultHeaders = new LinkedHashMap<>();

        this.defaultHeaders.put("Content-Type", "application/json");
        this.defaultHeaders.put("Accept", "application/json");
    }
    
    public ApiClient(String baseUrl) {
        this();
        this.baseUrl = baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDefaultHeaders(String key, String value) {
        this.defaultHeaders.put(key, value);
    }

    public void addAuthorizationBearer(String token) {
        this.defaultHeaders.put("Authorization", "Bearer " + token);
    }

    // get request
    public HttpResponse get(String endpoint) throws Exception {
        HttpRequest request = new HttpRequest("GET", buildUrl(endpoint));
        request.setHeaders(this.defaultHeaders);;
        return execute(request);
    }

    // post request
    public HttpResponse post(String endpoint, String body) throws Exception {
        HttpRequest request = new HttpRequest("POST", buildUrl(endpoint));
        
        // request.setHeaders(headers);
        request.setBody(body != null ? body : "");

        return execute(request);
    }

    // put method
    public HttpResponse put(String endpoint, String body) throws Exception {
        HttpRequest request = new HttpRequest("PUT", buildUrl(endpoint));

        request.setBody(body != null ? body : "");

        return execute(request);
    }

    // delete method
    public HttpResponse delete(String endpoint) throws Exception {
        HttpRequest request = new HttpRequest("DELETE", buildUrl(endpoint));

        return execute(request);
    }

    private String buildUrl(String endpoint) {
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        } else {
            return baseUrl + (endpoint.startsWith("/") ? endpoint : "/" + endpoint);
        }
    }

    public HttpResponse execute(HttpRequest request) throws Exception {
        long startTime = System.currentTimeMillis();

        URL url = new URL(request.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(request.getMethod());
        connection.setConnectTimeout(request.getTimeOut());

        // Set headers
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        // Set body if present
        if (request.getMethod().equalsIgnoreCase("POST") ||
                request.getMethod().equalsIgnoreCase("PUT") ||
                request.getMethod().equalsIgnoreCase("PATCH")) {
            if (!request.getBody().isEmpty()) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = request.getBody().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
        }

        int statusCode = connection.getResponseCode();

        // Read response body
        String body;
        try {
            InputStream is = (statusCode >= 400) ? connection.getErrorStream() : connection.getInputStream();
            if (is != null) {
                body = readStream(is);
                is.close();
            } else {
                body = "";
            }
        } catch (Exception e) {
            body = "";
        }

        // Extract response headers
        // Map<String, String> responseHeaders = new LinkedHashMap<>();
        // connection.getHeaderFields().forEach((key, values) -> {
        //     if (key != null && !values.isEmpty()) {
        //         responseHeaders.put(key, values.get(0));
        //     }
        // });

        long endTime = System.currentTimeMillis();
        connection.disconnect();

        return new HttpResponse(statusCode, body, endTime - startTime);
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }
}