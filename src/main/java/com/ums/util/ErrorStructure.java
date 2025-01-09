package com.ums.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorStructure {

	private Integer statusCode;
	private String message;
	private String rootCause ;
}
