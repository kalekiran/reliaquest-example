package com.example.rqchallenge.client.exception.resolver;

public class ErrorResponse {
	
	private int errorCode;
	private String errorMsg;

	public ErrorResponse() {
	}

	public ErrorResponse(String message) {
		this.errorMsg = errorMsg;
	}

	public ErrorResponse(int errorCode, String errorMsg) {
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
