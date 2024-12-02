package com.ssafy.enjoytrip.trip.model;

public class SidoDto {
	int sidoCode;
	String sidoName;

	public SidoDto() {
	}

	public SidoDto(int sidoCode, String sidoName) {
		this.sidoCode = sidoCode;
		this.sidoName = sidoName;
	}

	public int getSidoCode() {
		return sidoCode;
	}

	public void setSidoCode(int sidoCode) {
		this.sidoCode = sidoCode;
	}

	public String getSidoName() {
		return sidoName;
	}

	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}

	@Override
	public String toString() {
		return "Sido [sidoCode = " + sidoCode + ", sidoName = " + sidoName + "]";
	}
}
