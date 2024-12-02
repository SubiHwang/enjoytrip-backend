package com.ssafy.enjoytrip.tripdiary.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.exception.CustomException;
import com.ssafy.enjoytrip.exception.ErrorCode;
import com.ssafy.enjoytrip.members.model.MemberResponseDto;
import com.ssafy.enjoytrip.members.model.service.MemberService;
import com.ssafy.enjoytrip.trip.model.TripDto;
import com.ssafy.enjoytrip.trip.model.service.TripService;
import com.ssafy.enjoytrip.tripdiary.model.CreatedTripDiaryResponseDto;
import com.ssafy.enjoytrip.tripdiary.model.TimeDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryCardDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryRequestDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryResponseDto;
import com.ssafy.enjoytrip.tripdiary.model.TripRouteDto;
import com.ssafy.enjoytrip.tripdiary.model.TripRouteRequestDto;
import com.ssafy.enjoytrip.tripdiary.model.TripRouteResponseDto;
import com.ssafy.enjoytrip.tripdiary.model.mapper.TripDiaryMapper;

@Service
public class TripDiaryServiceImpl implements TripDiaryService {

	private final TripDiaryMapper tripDiaryMapper;
	private final MemberService memberService;
	private final TripService tripService;

	public TripDiaryServiceImpl(TripDiaryMapper tripDiaryMapper, MemberService memberService, TripService tripService) {
		super();
		this.tripDiaryMapper = tripDiaryMapper;
		this.memberService = memberService;
		this.tripService = tripService;
	}

	@Override
	public List<TripDiaryCardDto> getAllTripDiaries(String loginId) throws Exception {
		List<TripDiaryCardDto> cards = tripDiaryMapper.getAllTripDiaries();
		for (TripDiaryCardDto card : cards) {
			if (checkLikeStatus(loginId, card.getDiaryId())) { // 좋아요 게시물 확인
				card.setLike(true);
			}
		}
		return cards;
	}

	@Override
	public List<TripDiaryCardDto> getFollowingTripDiaries(String loginId) throws Exception {
		List<TripDiaryCardDto> cards = tripDiaryMapper.getFollowingTripDiaries(loginId);
		for (TripDiaryCardDto card : cards) {
			if (checkLikeStatus(loginId, card.getDiaryId())) { // 좋아요 게시물 확인
				card.setLike(true);
			}
		}
		return cards;
	}

	@Override
	public List<TripDiaryCardDto> getAccountTripDiaries(String userId, String loginId) throws Exception {
		List<TripDiaryCardDto> cards = tripDiaryMapper.getMyTripDiaries(userId);
		for (TripDiaryCardDto card : cards) {
			if (checkLikeStatus(loginId, card.getDiaryId())) { // 좋아요 게시물 확인
				card.setLike(true);
			}
			if(loginId.equals(card.getUserId())) { // 본인 게시물인지 확인
				card.setOwned(true);
			}
		}
		return cards;
	}

	@Override
	public CreatedTripDiaryResponseDto createTripDiary(String userId, TripDiaryRequestDto tripDiaryRequestDto) {
		// tripDiarys 저장
		TripDiaryDto tripDiaryDto = new TripDiaryDto();
		tripDiaryDto.setUserId(userId);
		tripDiaryDto.setTitle(tripDiaryRequestDto.getTitle());
		tripDiaryDto.setContent(tripDiaryRequestDto.getContent());
		tripDiaryDto.setThumbnailUrl(tripDiaryRequestDto.getThumbnailUrl());
		tripDiaryDto.setStartDate(tripDiaryRequestDto.getStartDate());
		tripDiaryDto.setEndDate(tripDiaryRequestDto.getEndDate());
		tripDiaryDto.setHasMap(tripDiaryRequestDto.getUseMap());
		tripDiaryDto.setTotalDays(tripDiaryRequestDto.getTotalDays());
		int result = tripDiaryMapper.createTripDiary(tripDiaryDto);

		if (result == 0) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}
		
		int createDairyId = tripDiaryDto.getDiaryId();

		// useMap을 사용하는 경우, places 정보를 triproutes에 저장
		result = 0;
		if (tripDiaryRequestDto.getUseMap() == true) {
			for (TripRouteRequestDto tripRoute : tripDiaryRequestDto.getPlaces()) {
				TripRouteDto tripRouteDto = new TripRouteDto();
				tripRouteDto.setDiaryId(createDairyId); // insert문 성공시 전달한 파라미터의 diaryId 필드에 PK 자동저장
				tripRouteDto.setAttractionsNo(tripRoute.getNo());
				tripRouteDto.setDay(tripRoute.getDay());
				tripRouteDto.setVisitOrder(tripRoute.getVisitOrder());
				tripRouteDto.setDetailStartTime(formatTime(tripRoute.getStartTime()));
				tripRouteDto.setDetailEndTime(formatTime(tripRoute.getEndTime()));
				result = tripDiaryMapper.createTripRoute(tripRouteDto);

				if (result == 0) {
					throw new CustomException(ErrorCode.SERVER_ERROR);
				}
			}
		}

		CreatedTripDiaryResponseDto createdTripDiary = new CreatedTripDiaryResponseDto(createDairyId);
		return createdTripDiary;
	}

	@Override
	public TripDiaryResponseDto getTripDiary(String userId, int diaryId) {
		TripDiaryResponseDto tripDiaryResponseDto = new TripDiaryResponseDto();

		// 1. 여행 일기 기본 정보 가져오기
		TripDiaryDto tripDiary = tripDiaryMapper.getTripDiary(diaryId);

		if (tripDiary == null) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		// 2. 여행 일기 기본 정보 설정
		tripDiaryResponseDto.setDiaryId(tripDiary.getDiaryId());
		tripDiaryResponseDto.setTitle(tripDiary.getTitle());
		tripDiaryResponseDto.setContent(tripDiary.getContent());
		tripDiaryResponseDto.setThumbnailUrl(tripDiary.getThumbnailUrl());
		tripDiaryResponseDto.setStartDate(tripDiary.getStartDate());
		tripDiaryResponseDto.setEndDate(tripDiary.getEndDate());
		tripDiaryResponseDto.setHasMap(tripDiary.isHasMap());
		tripDiaryResponseDto.setPublic(tripDiary.isPublic());
		tripDiaryResponseDto.setCreatedAt(tripDiary.getCreatedAt());
		tripDiaryResponseDto.setTotalDays(tripDiary.getTotalDays());

		// 3. 조회자가 여행 일기 좋아요 했는지 확인
		if(checkLikeStatus(userId, diaryId)) {
			tripDiaryResponseDto.setLike(true);
		}

		// 4. 작성자 정보 설정
		MemberResponseDto member = memberService.getMyProfileInfo(tripDiary.getUserId());
		if (member == null) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		if (userId.equals(tripDiary.getUserId())) {
			tripDiaryResponseDto.setOwned(true);
		} else {
			tripDiaryResponseDto.setOwned(false);
		}
		tripDiaryResponseDto.setMember(member);

		// 5. 지도 사용하는 경우에만 여행 경로 정보 가져오기
		if (tripDiary.isHasMap()) {
			List<TripRouteDto> tripRoutes = tripDiaryMapper.getTripRoute(diaryId);
			List<TripRouteResponseDto> tripRouteResponseDtos = new ArrayList<>();

			// 6. 각 경로에 대한 정보 설정
			for (TripRouteDto route : tripRoutes) {
				TripRouteResponseDto routeResponseDto = new TripRouteResponseDto();
				routeResponseDto.setRouteId(route.getRouteId());
				routeResponseDto.setDiaryId(route.getDiaryId());
				routeResponseDto.setDetailStartTime(route.getDetailStartTime());
				routeResponseDto.setDetailEndTime(route.getDetailEndTime());
				routeResponseDto.setDay(route.getDay());
				routeResponseDto.setVisitOrder(route.getVisitOrder());
				routeResponseDto.setCreatedAt(route.getCreatedAt());

				// 7. 관광지 정보 설정
				TripDto attraction = tripService.getAttractionByAttractionNo(route.getAttractionsNo());
				if (attraction == null) {
					throw new CustomException(ErrorCode.NOT_FOUND);
				}

				routeResponseDto.setAttraction(attraction);
				tripRouteResponseDtos.add(routeResponseDto);
			}

			tripDiaryResponseDto.setTripRoutes(tripRouteResponseDtos);
		}

		return tripDiaryResponseDto;
	}

	@Override
	@Transactional
	public int updateTripDiary(String userId, int diaryId, TripDiaryRequestDto tripDiaryRequestDto) {
		// 다이어리 존재 및 접근 권한 확인
		checkAuthrization(userId, diaryId);

		// tripDiarys 수정
		TripDiaryDto tripDiary = new TripDiaryDto();
		tripDiary.setDiaryId(diaryId);
		tripDiary.setUserId(userId);
		tripDiary.setTitle(tripDiaryRequestDto.getTitle());
		tripDiary.setContent(tripDiaryRequestDto.getContent());
		tripDiary.setThumbnailUrl(tripDiaryRequestDto.getThumbnailUrl());
		tripDiary.setStartDate(tripDiaryRequestDto.getStartDate());
		tripDiary.setEndDate(tripDiaryRequestDto.getEndDate());
		tripDiary.setHasMap(tripDiaryRequestDto.getUseMap());
		tripDiary.setTotalDays(tripDiaryRequestDto.getTotalDays());

		int result = tripDiaryMapper.updateTripDiary(tripDiary);
		if (result == 0) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}

		// 기존 tripRoutes 삭제
		result = tripDiaryMapper.deleteTripRoute(diaryId);

		// useMap을 사용하는 경우, places 정보를 triproutes에 저장
		if (tripDiaryRequestDto.getUseMap()) {
			for (TripRouteRequestDto tripRoute : tripDiaryRequestDto.getPlaces()) {
				TripRouteDto tripRouteDto = new TripRouteDto();
				tripRouteDto.setDiaryId(diaryId);
				tripRouteDto.setAttractionsNo(tripRoute.getNo());
				tripRouteDto.setDay(tripRoute.getDay());
				tripRouteDto.setVisitOrder(tripRoute.getVisitOrder());
				tripRouteDto.setDetailStartTime(formatTime(tripRoute.getStartTime()));
				tripRouteDto.setDetailEndTime(formatTime(tripRoute.getEndTime()));

				result = tripDiaryMapper.createTripRoute(tripRouteDto);
				if (result == 0) {
					throw new CustomException(ErrorCode.SERVER_ERROR);
				}
			}
		}

		return result;
	}

	@Override
	public int deleteTripDiary(String userId, int diaryId) {
		// 다이어리 존재 및 접근 권한 확인
		TripDiaryDto originTripDiary = checkAuthrization(userId, diaryId);

		// tripRoute 삭제
		int result = 0;
		if (originTripDiary.isHasMap()) {
			result = tripDiaryMapper.deleteTripRoute(diaryId);
			if (result == 0) {
				throw new CustomException(ErrorCode.SERVER_ERROR);
			}
		}

		// tripDiary 삭제
		result = tripDiaryMapper.deleteTripDiary(diaryId);
		if (result == 0) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}

		return result;
	}

	@Override
	public int updateShareStatusTripDiary(String userId, int diaryId, boolean shareStatus) {
		// 다이어리 존재 및 접근 권한 확인
		checkAuthrization(userId, diaryId);

		Map<String, Object> param = new HashMap<>();
		param.put("diaryId", diaryId);
		param.put("shareStatus", shareStatus);
		int result = tripDiaryMapper.updateShareStatusTripDiary(param);
		if (result == 0) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}

		return result;
	}

	@Override
	public int likeTripDiary(String userId, int diaryId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("diaryId", diaryId);

		int result = 0;
		try {
			result = tripDiaryMapper.likeTripDiary(param);
			if (result == 0) {
				throw new CustomException(ErrorCode.SERVER_ERROR);
			}
		} catch (DuplicateKeyException e) {
			throw new CustomException(ErrorCode.DUPLICATE_LIKE);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}

		return result;
	}

	@Override
	public int unlikeTripDiary(String userId, int diaryId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("diaryId", diaryId);

		int result = tripDiaryMapper.unlikeTripDiary(param);
		if (result == 0) {
			throw new CustomException(ErrorCode.DUPLICATE_LIKE);
		}

		return result;
	}

	@Override
	public TripDiaryDto checkAuthrization(String userId, int diaryId) {
		// 다이어리 존재 확인
		TripDiaryDto originTripDiary = tripDiaryMapper.getTripDiary(diaryId);
		if (originTripDiary == null) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		// 권한 확인
		if (!userId.equals(originTripDiary.getUserId())) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		return originTripDiary;
	}

	private boolean checkLikeStatus(String userId, int diaryId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("diaryId", diaryId);

		int count = 0;
		try {
			count = tripDiaryMapper.countLikeInfo(param);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}

		if (count == 0) {
			return false;
		}
		return true;
	}

	private String formatTime(TimeDto timeDto) {
		if (timeDto == null || timeDto.getHour().isEmpty() || timeDto.getMinute().isEmpty()
				|| timeDto.getPeriod().isEmpty()) {
			return null;
		}

		return String.format("%s:%s %s", timeDto.getHour(), timeDto.getMinute(), timeDto.getPeriod());
	}

	public TimeDto parseTime(String timeStr) {
		if (timeStr == null) {
			return null;
		}

		TimeDto timeDto = new TimeDto();
		String[] parts = timeStr.split(" ");

		if (parts.length == 2) {
			String[] timeParts = parts[0].split(":");
			timeDto.setHour(timeParts[0]);
			timeDto.setMinute(timeParts[1]);
			timeDto.setPeriod(parts[1]);
		}

		return timeDto;
	}
}
