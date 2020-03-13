package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SlotsNotInitializedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public SlotsNotInitializedException() {
		super(ErrorMessages.SLOT_NOT_INITIALIZED);
	}

}
