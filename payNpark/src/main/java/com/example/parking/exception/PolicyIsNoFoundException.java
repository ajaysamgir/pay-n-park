package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PolicyIsNoFoundException extends Exception {
	private static final long serialVersionUID = 7614594979597785589L;
	
	public PolicyIsNoFoundException() {
		super(ErrorMessages.POLICY_NOT_FOUND);
	}

}
