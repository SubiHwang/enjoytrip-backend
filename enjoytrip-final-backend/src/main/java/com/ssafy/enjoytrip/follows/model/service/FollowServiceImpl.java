package com.ssafy.enjoytrip.follows.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.follows.model.FollowListResponseDto;
import com.ssafy.enjoytrip.follows.model.FollowRequestDto;
import com.ssafy.enjoytrip.follows.model.FollowResponseDto;
import com.ssafy.enjoytrip.follows.model.mapper.FollowMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FollowServiceImpl implements FollowService {

	private final FollowMapper followMapper;

	public FollowServiceImpl(FollowMapper followMapper) {
		this.followMapper = followMapper;
	}

	@Override
	public int addFollow(String userId, FollowRequestDto requestDto) {

		if (userId.equals(requestDto.getFollowingId())) {
			throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
		}

		return followMapper.addFollow(userId, requestDto);
	}

	@Override
	public int deleteFollow(String userId, String followingId) {
		return followMapper.deleteFollow(userId, followingId);

	}

	@Override
	public FollowResponseDto getFollowInfo(String userId, String tokenId) {

		try {
			// 1. 유저 프로필 정보 가져오기
			String profileUrl = followMapper.getUserProfileUrl(userId);

			// 내 프로필 보는건지 체크! (요청한 userId와 현재 로그인한 currentUserId가 같으면 내꺼!)
			boolean isMyProfile = userId.equals(tokenId);

			// 2. 팔로잉/팔로워 수 카운트
			int followingCnt = followMapper.countFollowing(userId);
			int followerCnt = followMapper.countFollowers(userId);

			// 3. 현재 로그인한 유저(currentUserId)가
			// 조회하는 유저(userId)를 팔로우하고 있는지 체크
			// 내 프로필이면 무조건 true, 아니면 팔로우 여부 체크
			boolean isFollowing = isMyProfile ? true : followMapper.checkFollowing(userId, tokenId) > 0;

			System.out.println(userId);

			// 4. DTO로 싹 묶어서 리턴!
			return FollowResponseDto.builder().userId(userId).profileUrl(profileUrl).followingCnt(followingCnt)
					.followerCnt(followerCnt).isFollowing(isFollowing).build();

		} catch (Exception e) {
			log.error("팔로우 정보 조회 실패: {}", e.getMessage());
			throw new RuntimeException("팔로우 정보 조회 실패");
		}

	}

	@Override
	public Map<String, List<FollowListResponseDto>> getAllFollowLists(String userId) {

		// 팔로워와 팔로잉 목록을 각각 조회
		List<FollowListResponseDto> followers = followMapper.findFollowersByUserId(userId);
		List<FollowListResponseDto> following = followMapper.findFollowingsByUserId(userId);

		System.out.println("Followers size: " + (followers != null ? followers.size() : "null"));
        System.out.println("Following size: " + (following != null ? following.size() : "null"));

		// Map으로 묶어서 반환
		Map<String, List<FollowListResponseDto>> result = new HashMap<>();
		result.put("followers", followers);
		result.put("following", following);

		return result;
	}

}
