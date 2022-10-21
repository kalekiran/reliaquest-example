package com.example.rqchallenge.client;

import java.util.Arrays;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class RequestDetails {

    private final String url;
    private final HttpMethod method;
    private final MultiValueMap<String, String> queryParams;
    private final HttpHeaders requestHeaders;
    private final Object requestBody;

    public RequestDetails(String url, HttpMethod method) {
        this(url, method, new LinkedMultiValueMap<String, String>(), new HttpHeaders());
    }

    public RequestDetails(String url, HttpMethod method, MultiValueMap<String, String> queryParams) {
        this(url, method, queryParams, new HttpHeaders());
    }

    public RequestDetails(String url, HttpMethod method, MultiValueMap<String, String> queryParams,
            HttpHeaders headers) {

        this(url, method, queryParams, headers, null);

    }

    public RequestDetails(String url, HttpMethod method, MultiValueMap<String, String> queryParams, HttpHeaders headers,
            Object requestBody) {

        if (url == null || method == null) {
            throw new IllegalArgumentException("Invalid input. URL or method name cannot be null.");
        }

        if ((requestBody != null) && !isPayloadSupported(method)) {
            throw new IllegalArgumentException("Request payload not supported for method " + method.name());
        }
        this.url = url;
        this.method = method;
        this.queryParams = queryParams;
        this.requestHeaders = headers;
        this.requestBody = requestBody;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public MultiValueMap<String, String> getQueryParams() {
        return queryParams;
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public Object getRequestBody() {
        return requestBody;
    }
    /**
     * convenience method to set accept language in request headers
     */
    public void setAcceptLanguage () {
        requestHeaders.setAcceptLanguageAsLocales(Arrays.asList(LocaleContextHolder.getLocale()));      
    }

    @Override
    public String toString() {
        return "RequestDetails [url=" + url + ", method=" + method + ", queryParams=" + queryParams + ", headers="
                + requestHeaders + "]";
    }

    private boolean isPayloadSupported(HttpMethod method) {
        return (method.equals(HttpMethod.PUT) || method.equals(HttpMethod.POST));
    }

}