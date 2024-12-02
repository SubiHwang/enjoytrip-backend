package com.ssafy.enjoytrip.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoytrip.exception.CustomException;
import com.ssafy.enjoytrip.exception.ErrorCode;
import com.ssafy.enjoytrip.file.model.ImageFileUploadResponse;
import com.ssafy.enjoytrip.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name ="파 컨트롤러", description = "파일 업로드 등 클라우드 서버의 파일을 관리하는 컨트롤러")
public class FiltController {
	
	private final S3Service s3Service;

	public FiltController(S3Service s3Service) {
		super();
		this.s3Service = s3Service;
	}
	
	@Operation(summary = "파일 업로드", description = "파일을 업로드합니다.")
    @PostMapping("/upload/image")
    public ResponseEntity<?> fileUpload(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "type", defaultValue = "image") String type
    ) {
        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
            	log.debug("파일이 존재하지 않음");
                throw new CustomException(ErrorCode.EMPTY_FILE);
            }

            // 파일 타입 검사 (이미지인 경우)
            if (type.equals("image") && !file.getContentType().startsWith("image/")) {
            	log.debug("이미지 타입이 아님");
            	throw new CustomException(ErrorCode.NOT_IMAGE_FILE);
            }

            // 파일 크기 검사 (예: 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
            	log.debug("파일 크기가 10MB보다 큼");
            	throw new CustomException(ErrorCode.OVER_MAX_SIZE);
            }

            // 파일 업로드 및 URL 받기
            String fileUrl = s3Service.uploadImage(file);

            // 성공 응답
            return ResponseEntity.ok(new ImageFileUploadResponse(fileUrl));
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생", e);
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
    }
	
}
