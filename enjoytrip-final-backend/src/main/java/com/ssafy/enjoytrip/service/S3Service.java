
package com.ssafy.enjoytrip.service;

//기존 imports
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;  // stream() 사용을 위해 추가

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;  // StringUtils.isEmpty() 사용을 위해 추가
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 s3Client;
	private final String bucketName = "enjoy-trip";

	// 이미지 업로드 -> URL 반환
	public String uploadImage(MultipartFile file) {
		File convertedFile = null;
		try {
			// 파일명 중복 방지를 위한 생성
			String fileName = "I_love_trip_so_much" + file.getOriginalFilename();

			// MultipartFile -> File 변환
			convertedFile = convertMultiPartFileToFile(file);

			// S3에 업로드
			s3Client.putObject(bucketName, fileName, convertedFile);

			// ACL 설정
			setObjectACL(fileName);

			return String.format("https://kr.object.ncloudstorage.com/%s/%s", bucketName, fileName);
		} catch (Exception e) {
			throw new RuntimeException("이미지 업로드 실패", e);
		} finally {
			// 임시 파일이 항상 삭제되도록 보장
			if (convertedFile != null && convertedFile.exists()) {
				boolean deleted = convertedFile.delete();
				if (!deleted) {
					// 로그 추가
					log.warn("임시 파일 삭제 실패: {}", convertedFile.getAbsolutePath());
				}
			}
		}
	}

	// 이전 프로필 이미지 삭제
	public void deleteImage(String imageUrl) {
		try {
			String fileName = extractFileNameFromUrl(imageUrl);

			// 1. 파일 존재 여부 확인
			if (!s3Client.doesObjectExist(bucketName, fileName)) {
				log.warn("삭제할 파일이 존재하지 않음: {}", fileName);
				return; // 또는 예외 throw
			}

			// 2. 파일 정보 확인 (선택적)
			ObjectMetadata metadata = s3Client.getObjectMetadata(bucketName, fileName);
			log.info("삭제 예정 파일 - 크기: {}, 타입: {}", metadata.getContentLength(), metadata.getContentType());

			// 3. 파일 삭제
			s3Client.deleteObject(bucketName, fileName);
			log.info("파일 삭제 완료: {}", fileName);

		} catch (AmazonS3Exception e) {
			// S3 특정 에러 처리
			log.error("S3 에러 발생: {}, 에러코드: {}", e.getMessage(), e.getErrorCode());
			throw new RuntimeException("S3 파일 삭제 실패", e);
		} catch (Exception e) {
			log.error("예상치 못한 에러: {}", e.getMessage());
			throw new RuntimeException("파일 삭제 중 에러 발생", e);
		}
	}

	// MultipartFile -> File 변환
	private File convertMultiPartFileToFile(MultipartFile file) {
	    // 1. 입력값 검증
	    if (file == null || file.isEmpty()) {
	        throw new IllegalArgumentException("파일이 비어있거나 null입니다.");
	    }

	    // 2. 임시 디렉토리 및 파일명 설정
	    String tmpDir = System.getProperty("java.io.tmpdir");  // 시스템의 임시 디렉토리 경로
	    String originalFilename = file.getOriginalFilename();    
	    String safeFileName = generateSafeFileName(originalFilename);  // 안전한 파일명 생성
	    
	    // 3. 임시 파일 생성
	    File convertedFile = new File(tmpDir, safeFileName);

	    // 4. 파일 변환 (try-with-resources 사용)
	    try (InputStream inputStream = file.getInputStream();         // 자동 close
	         FileOutputStream fos = new FileOutputStream(convertedFile)) {  // 자동 close
	        
	        // 5. 버퍼 설정
	        byte[] buffer = new byte[8192];  // 8KB 버퍼 (성능 최적화)
	        int bytesRead;
	        long totalBytesRead = 0;
	        long fileSize = file.getSize();
	        
	        // 6. 파일 복사 및 진행률 체크
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            fos.write(buffer, 0, bytesRead);
	            totalBytesRead += bytesRead;
	            
	            // 10% 단위로 진행률 로깅
	            if (totalBytesRead % (fileSize / 10) < buffer.length) {
	                log.debug("파일 변환 진행률: {}%", (totalBytesRead * 100) / fileSize);
	            }
	        }
	        
	        log.info("파일 변환 완료: {} -> {}", originalFilename, convertedFile.getAbsolutePath());
	        return convertedFile;
	        
	    } catch (IOException e) {
	        // 7. 오류 발생 시 임시 파일 정리
	        if (convertedFile.exists()) {
	            if (!convertedFile.delete()) {
	                log.warn("임시 파일 삭제 실패: {}", convertedFile.getAbsolutePath());
	            }
	        }
	        throw new RuntimeException("파일 변환 중 오류 발생: " + originalFilename, e);
	    }
	}

	/**
	 * 안전한 파일명 생성 메소드
	 */
	private String generateSafeFileName(String originalFilename) {
	    // 1. null 체크
	    if (originalFilename == null) {
	        return UUID.randomUUID().toString();
	    }
	    
	    // 2. 확장자 추출
	    String extension = "";
	    int extensionIndex = originalFilename.lastIndexOf('.');
	    if (extensionIndex > 0) {
	        extension = originalFilename.substring(extensionIndex);  // .jpg, .png 등
	    }
	    
	    // 3. 파일명 정제
	    String baseFileName = originalFilename.substring(0, 
	                         extensionIndex > 0 ? extensionIndex : originalFilename.length())
	            .replaceAll("[^a-zA-Z0-9가-힣]", "_")  // 특수문자 제거
	            .replaceAll("\\s+", "_");              // 공백을 언더스코어로 변경
	    
	    // 4. UUID 추가하여 중복 방지
	    return baseFileName + "_" + UUID.randomUUID().toString() + extension;
	}

	/**
	 * S3 객체의 ACL(Access Control List) 설정 접근 권한 관리 및 오류 처리 개선
	 */
	private void setObjectACL(String objectName) {
		if (StringUtils.isEmpty(objectName)) {
			throw new IllegalArgumentException("객체 이름이 비어있습니다.");
		}

		try {
			// 객체 존재 여부 확인
			if (!s3Client.doesObjectExist(bucketName, objectName)) {
				throw new IllegalStateException("ACL 설정할 객체가 존재하지 않습니다: " + objectName);
			}

			// 현재 ACL 정보 가져오기
			AccessControlList currentAcl = s3Client.getObjectAcl(bucketName, objectName);

			// 현재 권한 로깅
			logCurrentPermissions(currentAcl);

			// 기존 퍼블릭 권한 제거 (보안 강화)
			removeExistingPublicPermissions(currentAcl);

			// 새로운 권한 설정
			currentAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

			// 변경된 ACL 적용
			s3Client.setObjectAcl(bucketName, objectName, currentAcl);

			// 설정 완료 후 재확인
			verifyAclSettings(objectName);

			log.info("ACL 설정 완료: {}", objectName);

		} catch (AmazonS3Exception e) {
			log.error("S3 ACL 설정 중 에러 발생: {}, 에러코드: {}", objectName, e.getErrorCode(), e);
			handleS3Exception(e);
		} catch (Exception e) {
			log.error("ACL 설정 중 예상치 못한 에러 발생: {}", objectName, e);
			throw new RuntimeException("ACL 설정 실패: " + objectName, e);
		}
	}

	/**
	 * 현재 설정된 권한 로깅
	 */
	private void logCurrentPermissions(AccessControlList acl) {
		log.debug("현재 ACL 설정:");
		for (Grant grant : acl.getGrantsAsList()) {
			log.debug("- 권한: {}, 대상: {}", grant.getPermission().toString(), grant.getGrantee().getIdentifier());
		}
	}

	/**
	 * 기존 퍼블릭 권한 제거
	 */
	private void removeExistingPublicPermissions(AccessControlList acl) {
		List<Grant> grantsToRemove = acl.getGrantsAsList().stream()
				.filter(grant -> grant.getGrantee() instanceof GroupGrantee).collect(Collectors.toList());

		for (Grant grant : grantsToRemove) {
			acl.revokeAllPermissions(grant.getGrantee());
			log.debug("제거된 권한: {}, 대상: {}", grant.getPermission(), grant.getGrantee().getIdentifier());
		}
	}

	/**
	 * ACL 설정 확인
	 */
	private void verifyAclSettings(String objectName) {
		AccessControlList verifyAcl = s3Client.getObjectAcl(bucketName, objectName);
		boolean hasPublicRead = verifyAcl.getGrantsAsList().stream().anyMatch(
				grant -> grant.getGrantee() == GroupGrantee.AllUsers && grant.getPermission() == Permission.Read);

		if (!hasPublicRead) {
			log.warn("ACL 설정 확인 실패: public read 권한이 없습니다.");
			throw new RuntimeException("ACL 설정 검증 실패");
		}
	}

	/**
	 * S3 예외 처리
	 */
	private void handleS3Exception(AmazonS3Exception e) {
		switch (e.getErrorCode()) {
		case "AccessDenied":
			throw new RuntimeException("S3 접근 권한이 없습니다.", e);
		case "NoSuchBucket":
			throw new RuntimeException("버킷이 존재하지 않습니다: " + bucketName, e);
		case "NoSuchKey":
			throw new RuntimeException("객체가 존재하지 않습니다.", e);
		default:
			throw new RuntimeException("S3 작업 중 에러 발생: " + e.getErrorCode(), e);
		}
	}

	// URL에서 파일명 추출
	private String extractFileNameFromUrl(String imageUrl) {
		return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
	}
}