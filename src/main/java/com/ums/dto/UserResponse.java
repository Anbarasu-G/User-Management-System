package com.ums.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

	private String username;
	private String email;
	private String password;
	private List<String> roles;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
