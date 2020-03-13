package com.example.parking.exception;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

public class CustomErrorHandler {
	private String timestamp;
	private String message;
	private List<String> errorDetails;
	private HttpStatus status; 

	public CustomErrorHandler() {
	}

	public CustomErrorHandler(String message, List<String> details, HttpStatus status,  String date) {
		super();
		this.timestamp = date;
		this.message = message;
		this.status = status;
		this.errorDetails = details;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(List<String> errorDetails) {
		this.errorDetails = errorDetails;
	}
}
