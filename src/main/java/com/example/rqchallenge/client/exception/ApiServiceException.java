package com.example.rqchallenge.client.exception;

public class ApiServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMsg;

	public ApiServiceException(Throwable t) {
		super(t);
	}

	public ApiServiceException(String message) {
		super(message);
	}

	public ApiServiceException(int errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
