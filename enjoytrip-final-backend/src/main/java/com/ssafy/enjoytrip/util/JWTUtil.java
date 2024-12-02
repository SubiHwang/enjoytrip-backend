package com.ssafy.enjoytrip.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JWTUtil {

	@Value("${jwt.salt}")
	private String salt;

	@Value("${jwt.access-token.expiretime}")
	private long accessTokenExpireTime;

	@Value("${jwt.refresh-token.expiretime}")
	private long refreshTokenExpireTime;

	public String createAccessToken(String memberId) {
		return create(memberId, "access-token", accessTokenExpireTime);
	}

	public String createRefreshToken(String memberId) {
		return create(memberId, "refresh-token", refreshTokenExpireTime);
	}

	private String create(String memberId, String subject, long expireTime) {
		// payload
		Claims claims = Jwts.claims().setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expireTime));

		// data
		claims.put("memberId", memberId);

		// jwt
		String jwt = Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, generateKey())
				.compact();

		return jwt;
	}

	private byte[] generateKey() {
		byte[] key = null;
		try {
			key = salt.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (log.isInfoEnabled()) {
				e.printStackTrace();
			} else {
				log.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}
		return key;
	}
	
	public boolean checkToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(generateKey()).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
	
	public String getMemberIdFromToken(String token) {
	    try {
	        Claims claims = Jwts.parser()
	                .setSigningKey(generateKey())
	                .parseClaimsJws(token)
	                .getBody();
	        
	        return claims.get("memberId", String.class);
	    } catch (Exception e) {
	        log.error("Failed to extract memberId from token: {}", e.getMessage());
	        return null;
	    }
	}
}
