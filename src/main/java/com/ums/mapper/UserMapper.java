package com.ums.mapper;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ums.dto.UserRequest;
import com.ums.dto.UserResponse;
import com.ums.entity.User;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {

	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public User mapToUser(UserRequest request) {

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRoles(request.getRoles());
		user.setCreatedAt(LocalDateTime.now());
		user.setModifiedAt(null);

		return user;
	}

	public UserResponse mapToUserResponse(User user) {

		UserResponse response = new UserResponse();
		
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setPassword(user.getPassword());
		response.setRoles(user.getRoles());
		response.setCreatedAt(user.getCreatedAt());
		response.setModifiedAt(user.getModifiedAt());

		return response;
	}
}
