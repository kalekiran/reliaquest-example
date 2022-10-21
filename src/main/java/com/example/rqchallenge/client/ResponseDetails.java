package com.example.rqchallenge.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ResponseDetails<R> {

	private final R responseBody;
	private final HttpStatus status;
	private final HttpHeaders responseHeaders;
	private final String errorMessage;

	public ResponseDetails(HttpStatus status) {
		this(status, new HttpHeaders(), null, null);
	}

	public ResponseDetails(HttpStatus status, HttpHeaders responseHeaders) {
		this(status, responseHeaders, null, null);
	}

	public ResponseDetails(HttpStatus status, HttpHeaders responseHeaders, R response, String errorMessage) {
		this.status = status;
		this.responseHeaders = responseHeaders;
		this.responseBody = response;
		this.errorMessage = errorMessage;
	}

	public R getResponseBody() {
		return responseBody;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public HttpHeaders getResponseHeaders() {
		return responseHeaders;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "ResponseDetails [responseBody=" + responseBody + ", status=" + status + ", responseHeaders="
				+ responseHeaders + ", errorMessage=" + errorMessage + "]";
	}
}
