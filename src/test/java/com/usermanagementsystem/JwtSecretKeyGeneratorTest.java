package com.usermanagementsystem;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

public class JwtSecretKeyGeneratorTest {

	@Test
	public void generateSecretKey() {
		
		SecretKey secretKey = Jwts.SIG.HS512.key().build();
		String key = DatatypeConverter.printBase64Binary(secretKey.getEncoded());
		
		System.out.println("Key : " + key);
	}
}
