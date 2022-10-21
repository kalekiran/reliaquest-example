package com.example.rqchallenge.client.exception;

public class IntraApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int httpStatusCode;
	private String errorMsg;

	public IntraApiException(Throwable t) {
		super(t);
	}

	public IntraApiException(String message) {
		super(message);
	}

	public IntraApiException(int httpStatusCode, String errorMsg) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.errorMsg = errorMsg;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
