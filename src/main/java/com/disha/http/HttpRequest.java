package com.disha.http;

import java.util.*;

public class HttpRequest {
    private String method;
    private String url;
    private String body;
    private Map<String, String> headers;
    private int timeOut;

    public HttpRequest(String method, String url) {
        this.method = method;
        this.url = url;
        this.body = "";
        this.headers = new LinkedHashMap<>();
        this.timeOut = 30000;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
