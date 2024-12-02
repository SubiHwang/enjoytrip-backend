package com.ssafy.enjoytrip.trip.model.service;

import java.util.List;

import com.ssafy.enjoytrip.trip.model.CategoryDto;
import com.ssafy.enjoytrip.trip.model.GugunDto;
import com.ssafy.enjoytrip.trip.model.SidoDto;
import com.ssafy.enjoytrip.trip.model.TripDto;
import com.ssafy.enjoytrip.trip.model.TripRequestDto;

public interface TripService {
	List<SidoDto> listSido() throws Exception;
	
	List<GugunDto> listGugun(int sidoCode) throws Exception;
	
	List<CategoryDto> listCategory() throws Exception;
	
	List<TripDto> listTrip(TripRequestDto tripRequestDto) throws Exception;

	TripDto getAttractionByAttractionNo(int attractionNo);
}
