package com.ums.util;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppResponseBuilder {

	public static <T> SuccessStructure<?> success(Integer statusCode, String message, T data) {

		SuccessStructure<T> structure = new SuccessStructure<T>();

		structure.setStatusCode(statusCode);
		structure.setMessage(message);
		structure.setData(data);

		return structure;
	}

	public ErrorStructure error(Integer statusCode, String message, String rootCause) {

		ErrorStructure structure = new ErrorStructure();

		structure.setStatusCode(statusCode);
		structure.setMessage(message);
		structure.setRootCause(rootCause);
		
		return structure;
	}
}
