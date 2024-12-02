package com.ssafy.enjoytrip.tripdiary.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoytrip.tripdiary.model.TripDiaryCardDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryDto;
import com.ssafy.enjoytrip.tripdiary.model.TripRouteDto;

@Mapper
public interface TripDiaryMapper {
	List<TripDiaryCardDto> getAllTripDiaries();
	
	List<TripDiaryCardDto> getFollowingTripDiaries(String userId);
	
	List<TripDiaryCardDto> getMyTripDiaries(String userId);
	
	int createTripDiary(TripDiaryDto tripDiaryDto);
	
	int createTripRoute(TripRouteDto tripRouteDto);
	
	TripDiaryDto getTripDiary(int diary_id);
	
	List<TripRouteDto> getTripRoute(int diary_id);
	
	int updateTripDiary(TripDiaryDto tripDiaryDto);
	
	int deleteTripDiary(int diaryId);
	
	int deleteTripRoute(int diaryId);
	
	int updateShareStatusTripDiary(Map<String, Object> param);
	
	TripDiaryDto checkAuthrization(String userId, int diaryId);
	
	int countLikeInfo(Map<String, Object> param);
	
	int likeTripDiary(Map<String, Object> param);
	
	int unlikeTripDiary(Map<String, Object> param);
}
