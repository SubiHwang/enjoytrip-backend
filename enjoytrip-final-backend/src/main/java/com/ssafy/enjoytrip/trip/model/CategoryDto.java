package com.ssafy.enjoytrip.trip.model;

public class CategoryDto {

	private int id;
	private String name;

	public CategoryDto() {
	}

	public CategoryDto(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [id = " + id + ", name = " + name + "]";
	}
}
