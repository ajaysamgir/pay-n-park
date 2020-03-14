package com.example.parking.exception;

public class ErrorMessages {
	public static final String SLOT_IS_NOT_FOUND = "Slot is not available to park your car";
	public static final String SERVER_ERROR = "Internal Server Error"; 
	public static final String BAD_REQUEST = "Bad Request please check mentioned field values once";
	public static final String INVALID_CAPACITY_ERROR = "totalCapacity value is not valid";
	public static final String INVALID_CAPACITY_HINT = "It should be greater than or equal to sum of all vehicles slot";
	public static final String SLOTS_ALREADY_INITIALIZED = "Parking Slots already initialized";
	public static final String INITILIZATION_DONE_BEFORE = "initialization already done";
	public static final String SLOT_NOT_INITIALIZED = "Slot is not allocated yet";
	public static final String POLICY_NOT_FOUND = "Policy is not found";
	public static final String SLOT_IS_FULL = "Not able to provide you parking slot as parking is full";
	public static final String CAR_ENTRY_EXIST = "Car Number which mentioned that is already exist";
	public static final String APP_NOT_INITIATED = "Application not initiated";
}
