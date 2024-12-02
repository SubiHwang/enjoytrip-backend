package com.ssafy.enjoytrip.trip.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDto {
	private int no;
	private String title;
	private String image;
	private double latitude;
	private double longitude;
	private String addr;

	public TripDto() {
	}

	public TripDto(int no, String title, String image, double latitude, double longitude, String addr) {
		this.no = no;
		this.title = title;
		this.image = image;
		this.latitude = latitude;
		this.longitude = longitude;
		this.addr = addr;
	}

	@Override
	public String toString() {
		return "AttractionDto [no=" + no + ", title=" + title + ", image=" + image + ", latitude=" + latitude + ", longitude="
				+ longitude + ", addr=" + addr + "]";
	}

}
