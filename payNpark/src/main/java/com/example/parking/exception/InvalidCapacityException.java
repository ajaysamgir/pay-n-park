package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCapacityException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCapacityException(int capacity) {
		super(ErrorMessages.INVALID_CAPACITY_ERROR + " (" + capacity + ") " + ErrorMessages.INVALID_CAPACITY_HINT);
	}

}