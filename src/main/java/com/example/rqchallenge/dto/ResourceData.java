package com.example.rqchallenge.dto;

public class ResourceData<R> {

	private R data;

	public ResourceData() {
	}

	public ResourceData(R data) {
		this.data = data;
	}

	public R getData() {
		return data;
	}

	public void setData(R data) {
		this.data = data;
	}

}
