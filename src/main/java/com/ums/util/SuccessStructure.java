package com.ums.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessStructure<T> {
	
	private Integer statusCode;
	private String message;
	private T data ;
}
