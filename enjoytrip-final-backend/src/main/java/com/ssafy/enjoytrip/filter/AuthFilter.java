package com.ssafy.enjoytrip.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.exception.CustomException;
import com.ssafy.enjoytrip.exception.ErrorCode;
import com.ssafy.enjoytrip.exception.ErrorResponse;
import com.ssafy.enjoytrip.util.JWTUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthFilter implements Filter {
	private final JWTUtil jwtUtil;
	private final AntPathMatcher pathMatcher;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private static final Map<String, List<String>> PUBLIC_URLS = Map.of(
			"GET", Arrays.asList(
					"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
					"/assets/**", "/webjars/**", "/favicon.ico", "/trip-diary/all", "/trip/**"),
			"POST", Arrays.asList(
					"/member/login", "/member/", "/graph"), 
			"PUT", Arrays.asList(), 
			"DELETE", Arrays.asList());

	public AuthFilter(JWTUtil jwtUtil, AntPathMatcher pathMatcher) {
		this.jwtUtil = jwtUtil;
		this.pathMatcher = pathMatcher;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    HttpServletResponse httpResponse = (HttpServletResponse) response;
	    
	    // CORS 헤더를 모든 응답에 추가
	    addCorsHeaders(httpRequest, httpResponse);
	    
	    String requestURI = httpRequest.getRequestURI();
	    String method = httpRequest.getMethod();

	    log.debug("requestURI = {}, method = {}", requestURI, method);

	    // CORS Preflight 요청 처리
	    if ("OPTIONS".equalsIgnoreCase(method)) {
	        httpResponse.setStatus(HttpServletResponse.SC_OK);
	        return;
	    }

	    // Public URL 체크
	    List<String> publicUrlsForMethod = PUBLIC_URLS.getOrDefault(method, Collections.emptyList());
	    if (publicUrlsForMethod.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI))) {
	        chain.doFilter(request, response);
	        return;
	    }

	    // 토큰 검증
	    try {
	        validateToken(httpRequest, httpResponse);
	        chain.doFilter(request, response);
	    } catch (CustomException e) {
	        sendErrorResponse(httpResponse, e);
	    }
	}

	// CORS 헤더를 추가하는 메소드를 분리
	private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
	    String origin = request.getHeader("Origin");
	    if (origin != null) {
	        response.setHeader("Access-Control-Allow-Origin", origin);
	        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
	        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
	        response.setHeader("Access-Control-Max-Age", "3600");
	    }
	}

	private void validateToken(HttpServletRequest request, HttpServletResponse response) {
	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        System.out.println("TOKEN_NOT_FOUND");
	        throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
	    }

	    String token = authHeader.substring(7);
	    if (!jwtUtil.checkToken(token)) {
	        System.out.println("token_expired");
	        throw new CustomException(ErrorCode.TOKEN_EXPIRED);
	    }
	}

	private void sendErrorResponse(HttpServletResponse response, CustomException ex) throws IOException {
		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getMessage(), ex.getErrorCode().getCode());

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}