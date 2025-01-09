package com.ums.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {

	private String message;

	@Override
	public String getMessage() {

		return message;
	}
}
