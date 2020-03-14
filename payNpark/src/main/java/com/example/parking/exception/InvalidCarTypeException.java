package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCarTypeException extends Exception {

	private static final long serialVersionUID = 1488086836075889433L;

	public InvalidCarTypeException(String message) {
		super(message);
	}

}
