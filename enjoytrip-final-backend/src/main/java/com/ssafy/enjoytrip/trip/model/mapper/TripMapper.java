package com.ssafy.enjoytrip.trip.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoytrip.trip.model.CategoryDto;
import com.ssafy.enjoytrip.trip.model.GugunDto;
import com.ssafy.enjoytrip.trip.model.SidoDto;
import com.ssafy.enjoytrip.trip.model.TripDto;
import com.ssafy.enjoytrip.trip.model.TripRequestDto;

@Mapper
public interface TripMapper {

	List<SidoDto> listSido();
	
	List<GugunDto> listGugun(int sidoCode);
	
	List<CategoryDto> listCategory();
	
	List<TripDto> listTrip(TripRequestDto tripRequestDto);
	
	TripDto getAttractionByAttractionNo(int attractionNo);
}
