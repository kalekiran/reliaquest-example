package com.example.rqchallenge.dto;

import java.util.List;

public class ResourceList<T> {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

	public ResourceList() {
	}

	public ResourceList(List<T> data) {
		this.data = data;
	}
}
