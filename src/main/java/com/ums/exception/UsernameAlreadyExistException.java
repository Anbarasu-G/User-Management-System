package com.ums.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsernameAlreadyExistException  extends RuntimeException{

	private final String message;

	@Override
	public String getMessage() {

		return message;
	}
}
