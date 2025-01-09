package com.ums.jwt;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	private  String secretKey; 
	private static final Long ACCESS_TOKEN_EXPIRY = TimeUnit.MINUTES.toMillis(1);
	private static final Long REFRESH_TOKEN_EXPIRY = TimeUnit.MINUTES.toMillis(10);

	public SecretKey generateKey() {

		byte[] decode = Base64.getDecoder().decode(secretKey);
		return	Keys.hmacShaKeyFor(decode);
	}

	public String getAccessToken(Authentication authentication) {

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("Type", "Access-Token");

		return	buildToken(authentication, claims, ACCESS_TOKEN_EXPIRY);
	}

	public String getRefreshToken(Authentication authentication) {

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("Type", "Refresh-Token");

		return	buildToken(authentication, claims, REFRESH_TOKEN_EXPIRY);
	}

	private String buildToken(Authentication authentication, Map<String, Object> claims, Long tokenExpiry) {

		return Jwts.builder()
				.claims(claims)
				.subject(authentication.getName())
				.signWith(generateKey())
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(tokenExpiry)))
				.compact();
	}

	public String extractUsername(String jwt) {

		return getClaims(jwt)
				.getSubject();
	}

	private Claims getClaims(String jwt) {
		return Jwts.parser()
				.verifyWith(generateKey())
				.build()
				.parseSignedClaims(jwt)
				.getPayload();
	}

	public boolean isValidToken(String jwt) {

		return getClaims(jwt).getExpiration().after(Date.from(Instant.now()));
	}
}
