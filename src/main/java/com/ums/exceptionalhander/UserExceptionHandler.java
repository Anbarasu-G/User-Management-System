package com.ums.exceptionalhander;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ums.exception.InvalidUsernameException;
import com.ums.exception.SessionExpiredException;
import com.ums.exception.UserNotFoundException;
import com.ums.exception.UsernameAlreadyExistException;
import com.ums.util.AppResponseBuilder;
import com.ums.util.ErrorStructure;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class UserExceptionHandler {

	private AppResponseBuilder builder;

	@ExceptionHandler(UsernameAlreadyExistException.class)
	public ErrorStructure handleUsernameAlreadyExist(UsernameAlreadyExistException exception){

		return builder.error(
				HttpStatus.ALREADY_REPORTED.value(),
				"Duplicate Username were not considered !!!",
				exception.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ErrorStructure handleUserNotFound(UserNotFoundException exception){

		return builder.error(
				HttpStatus.NOT_FOUND.value(),
				"No User Found !!!",
				exception.getMessage());
	}

	@ExceptionHandler(InvalidUsernameException.class)
	public ErrorStructure handleInvalidUsername(InvalidUsernameException exception) {

		return builder.error(
				HttpStatus.CONFLICT.value(),
				"Wrong Username !!!",
				exception.getMessage()
				);
	}
	
	@ExceptionHandler(SessionExpiredException.class)
	public ErrorStructure handleISessionExpired(SessionExpiredException exception) {

		return builder.error(
				HttpStatus.CONFLICT.value(),
				"Enter the OTP within 1 MIN !!!",
				exception.getMessage()
				);
	}
}
