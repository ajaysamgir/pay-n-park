package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarNotFoundInSlotException extends Exception {

	private static final long serialVersionUID = -1804242905940785261L;
	
	public CarNotFoundInSlotException(String message) {
		super(message);
	}

}
