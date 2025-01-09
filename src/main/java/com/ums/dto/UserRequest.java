package com.ums.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

	private String username;
	private String email;
	private String password;
	private List<String> roles;
}
