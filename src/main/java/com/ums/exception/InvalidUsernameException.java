package com.ums.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidUsernameException extends Exception {

	private String message;
	
	@Override
	public String getMessage() {
		return message;
	}
}
