package com.ums.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse  {

	private String accessToken;
	private String refreshToken;
}
