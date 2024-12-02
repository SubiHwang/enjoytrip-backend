package com.ssafy.enjoytrip.trip.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.trip.model.TripRequestDto;
import com.ssafy.enjoytrip.trip.model.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/trip")
@Tag(name = "관광지 컨트롤러", description = "전반적인 관광지 정보 조회를 처리하는 컨트롤러")
public class TripController {

	private TripService tripService;

	public TripController(TripService tripService) {
		this.tripService = tripService;
	}

	@Operation(summary = "전국 관광지 정보 조회", description = "전국 관광지 정보를 반환해줍니다.")
	@GetMapping(value = "")
	public ResponseEntity<?> listTrip(TripRequestDto tripRequestDto) throws Exception {
		log.info("전국 관광지 정보 조회 요청 : {}", tripRequestDto);
		
		if(tripRequestDto.getSidoCode() == 0
				&& tripRequestDto.getGugunCode() == 0
				&& tripRequestDto.getCategory() == 0
				&& tripRequestDto.getKeyword() == null) {
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body("");
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(tripService.listTrip(tripRequestDto));
	}
	
	@Operation(summary = "시도 정보 조회", description = "시도 정보를 반환해줍니다.")
	@GetMapping(value = "/sido")
	public ResponseEntity<?> listSido() throws Exception {
		log.info("시도 정보 조회 요청");
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(tripService.listSido());
	}
	
	@Operation(summary = "구군 정보 조회", description = "구군 정보를 반환해줍니다.")
	@GetMapping(value = "/gugun")
	public ResponseEntity<?> listGugun(
			@Parameter(name = "sidoCode", example = "1")
			@RequestParam("sidoCode") Integer sidoCode) throws Exception {
		log.info("구군 정보 조회 요청 : sidoCode = " + sidoCode);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(tripService.listGugun(sidoCode));
	}
	
	@Operation(summary = "관광지 유형 정보 조회", description = "관광지 유형 정보를 반환해줍니다.")
	@GetMapping(value = "/category")
	public ResponseEntity<?> listCategory() throws Exception {
		log.info("관광지 유형 정보 조회 요청");
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(tripService.listCategory());
	}

}
