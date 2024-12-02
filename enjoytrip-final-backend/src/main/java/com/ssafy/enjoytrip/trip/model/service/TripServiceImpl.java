package com.ssafy.enjoytrip.trip.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.trip.model.CategoryDto;
import com.ssafy.enjoytrip.trip.model.GugunDto;
import com.ssafy.enjoytrip.trip.model.SidoDto;
import com.ssafy.enjoytrip.trip.model.TripDto;
import com.ssafy.enjoytrip.trip.model.TripRequestDto;
import com.ssafy.enjoytrip.trip.model.mapper.TripMapper;

@Service
public class TripServiceImpl implements TripService {
	
	private final TripMapper tripMapper;

	public TripServiceImpl(TripMapper tripMapper) {
		super();
		this.tripMapper = tripMapper;
	}

	@Override
	public List<SidoDto> listSido() throws Exception {
		return tripMapper.listSido();
	}

	@Override
	public List<GugunDto> listGugun(int sidoCode) throws Exception {
		return tripMapper.listGugun(sidoCode);
	}

	@Override
	public List<CategoryDto> listCategory() throws Exception {
		return tripMapper.listCategory();
	}

	@Override
	public List<TripDto> listTrip(TripRequestDto tripRequestDto) throws Exception {
		return tripMapper.listTrip(tripRequestDto);
	}
	
	public TripDto getAttractionByAttractionNo(int attractionNo) {
		return tripMapper.getAttractionByAttractionNo(attractionNo);
	}
}
