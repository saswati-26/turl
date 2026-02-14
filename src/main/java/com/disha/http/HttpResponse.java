package com.disha.http;

public class HttpResponse {
    private int statusCode;
    private String body;
    private long responseTime;

    public HttpResponse(int statusCode, String body, long responseTime) {
        this.statusCode = statusCode;
        this.body = body;
        this.responseTime = responseTime;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getBody() {
        return this.body;
    }

    public long getResponseTime() {
        return this.responseTime;
    }

    public boolean isSuccess() {
        return this.statusCode >= 200 && this.statusCode < 300;
    }

    public boolean isRedirect() {
        return this.statusCode >= 300 && this.statusCode< 400;
    }

    public boolean isClientError() {
        return this.statusCode >= 400 && this.statusCode < 500;
    }

    public boolean isServerError() {
        return this.statusCode >= 500;
    }
}
