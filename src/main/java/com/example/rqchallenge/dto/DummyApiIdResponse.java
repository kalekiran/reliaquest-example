package com.example.rqchallenge.dto;

public class DummyApiIdResponse {

	private String status;
	
	private Employee data;
	
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Employee getData() {
		return data;
	}

	public void setData(Employee data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
