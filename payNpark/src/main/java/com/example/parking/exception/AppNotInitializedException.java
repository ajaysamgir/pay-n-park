package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AppNotInitializedException extends Exception {

	private static final long serialVersionUID = 3139007414327752575L;
	
	public AppNotInitializedException(String message) {
		super(message);
	}

}
