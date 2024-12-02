package com.ssafy.enjoytrip.tripdiary.model.service;

import java.util.List;

import com.ssafy.enjoytrip.tripdiary.model.CreatedTripDiaryResponseDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryCardDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryRequestDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryResponseDto;

public interface TripDiaryService {
	
	List<TripDiaryCardDto> getAllTripDiaries(String loginId) throws Exception;
	
	List<TripDiaryCardDto> getFollowingTripDiaries(String loginId) throws Exception;
	
	List<TripDiaryCardDto> getAccountTripDiaries(String userId, String loginId) throws Exception;
	
	CreatedTripDiaryResponseDto createTripDiary(String userId, TripDiaryRequestDto tripDiaryRequestDto);
	
	TripDiaryResponseDto getTripDiary(String userId, int diary_id);
	
	int updateTripDiary(String userId, int diaryId, TripDiaryRequestDto tripDiaryRequestDto);
	
	int deleteTripDiary(String userId, int diaryId);
	
	int updateShareStatusTripDiary(String userId, int diaryId, boolean shareStatus);
	
	TripDiaryDto checkAuthrization(String userId, int diaryId);
	
	int likeTripDiary(String userId, int diaryId);
	
	int unlikeTripDiary(String userId, int diaryId);
}
