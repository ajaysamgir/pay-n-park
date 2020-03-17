package com.example.parking.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler({ SlotNotFoundException.class, InvalidCapacityException.class, AllReadyInitializedException.class,
			SlotsNotInitializedException.class, PolicyIsNoFoundException.class, CarEntryAllreayExistException.class,
			CarNotFoundInSlotException.class, InvalidCarTypeException.class, AppNotInitializedException.class })
	@Nullable
	public final ResponseEntity<CustomErrorHandler> handleException(Exception ex, WebRequest request) {
		if (ex instanceof SlotNotFoundException) {
			SlotNotFoundException exp = (SlotNotFoundException) ex;
			return handleSlotNotFoundException(exp, request);
		} else if (ex instanceof InvalidCapacityException) {
			InvalidCapacityException exp = (InvalidCapacityException) ex;
			return handleInvalidCapacityException(exp, request);
		} else if (ex instanceof AllReadyInitializedException) {
			AllReadyInitializedException exp = (AllReadyInitializedException) ex;
			return handleAllReadyInitializedException(exp, request);
		} else if (ex instanceof SlotsNotInitializedException) {
			SlotsNotInitializedException exp = (SlotsNotInitializedException) ex;
			return handleSlotsNotInitializedException(exp, request);
		} else if (ex instanceof PolicyIsNoFoundException) {
			PolicyIsNoFoundException exp = (PolicyIsNoFoundException) ex;
			return handlePolicyIsNoFoundException(exp, request);
		} else if (ex instanceof CarEntryAllreayExistException) {
			CarEntryAllreayExistException exp = (CarEntryAllreayExistException) ex;
			return handleCarEntryAllreayExistException(exp, request);
		} else if (ex instanceof InvalidCarTypeException) {
			InvalidCarTypeException exp = (InvalidCarTypeException) ex;
			return handleInvalidCarTypeException(exp, request);
		} else if (ex instanceof AppNotInitializedException) {
			AppNotInitializedException exp = (AppNotInitializedException) ex;
			return handleAppNotInitializedException(exp, request);
		} else if (ex instanceof CarNotFoundInSlotException) {
			CarNotFoundInSlotException exp = (CarNotFoundInSlotException) ex;
			return handleCarNotFoundInSlotException(exp, request);
		} else {
			return handleExceptionInternal(ex, request);
		}
	}

	private ResponseEntity<CustomErrorHandler> handleCarNotFoundInSlotException(CarNotFoundInSlotException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.CAR_NOT_FOUND_IN_SLOT, details,
				HttpStatus.NOT_FOUND, new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<CustomErrorHandler> handleAppNotInitializedException(AppNotInitializedException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.APP_NOT_INITIATED, details,
				HttpStatus.NOT_ACCEPTABLE, new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
	}

	private ResponseEntity<CustomErrorHandler> handleInvalidCarTypeException(InvalidCarTypeException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.INVALID_CAR_TYPE, details, HttpStatus.NOT_FOUND,
				new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<CustomErrorHandler> handleCarEntryAllreayExistException(CarEntryAllreayExistException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.CAR_ENTRY_EXIST, details,
				HttpStatus.ALREADY_REPORTED, new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.ALREADY_REPORTED);
	}

	private ResponseEntity<CustomErrorHandler> handlePolicyIsNoFoundException(PolicyIsNoFoundException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.POLICY_NOT_FOUND, details, HttpStatus.NOT_FOUND,
				new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<CustomErrorHandler> handleSlotsNotInitializedException(SlotsNotInitializedException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.SLOTS_ALREADY_INITIALIZED, details,
				HttpStatus.NOT_FOUND, new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<CustomErrorHandler> handleAllReadyInitializedException(AllReadyInitializedException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.INITILIZATION_DONE_BEFORE, details,
				HttpStatus.NOT_ACCEPTABLE, new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
	}

	private ResponseEntity<CustomErrorHandler> handleInvalidCapacityException(InvalidCapacityException exp,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exp.getMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.BAD_REQUEST, details, HttpStatus.BAD_REQUEST,
				new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<CustomErrorHandler> handleExceptionInternal(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		CustomErrorHandler errorHandler = new CustomErrorHandler(ex.getMessage(), details,
				HttpStatus.INTERNAL_SERVER_ERROR, new Date().toString());
		return new ResponseEntity<>(errorHandler, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private final ResponseEntity<CustomErrorHandler> handleSlotNotFoundException(SlotNotFoundException ex,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		CustomErrorHandler error = new CustomErrorHandler(ErrorMessages.SLOT_IS_NOT_FOUND, details,
				HttpStatus.NOT_FOUND, new Date().toString());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}
