package com.example.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SlotNotFoundException extends Exception {
	private static final long serialVersionUID = -6895517221082649703L;
	
	public SlotNotFoundException(String exception) {
        super(exception);
    }
}
