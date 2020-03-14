package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class CarEntryAllreayExistException extends Exception {

	private static final long serialVersionUID = -2972189316234203838L;

	public CarEntryAllreayExistException(String message) {
		super(message);
	}
}