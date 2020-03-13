package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AllReadyInitializedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AllReadyInitializedException() {
		super(ErrorMessages.SLOTS_ALREADY_INITIALIZED);
	}

}
