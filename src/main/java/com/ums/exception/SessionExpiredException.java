package com.ums.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionExpiredException extends Exception {

	private String message;

	@Override
	public String getMessage() {

		return getMessage();
	}
}
