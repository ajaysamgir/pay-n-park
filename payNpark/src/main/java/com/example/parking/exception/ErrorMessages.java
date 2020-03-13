package com.example.parking.exception;

public class ErrorMessages {
	static final String SLOT_IS_NOT_FOUND = "Slot is not available to park your car";
    static final String SERVER_ERROR = "Internal Server Error"; 
    static final String BAD_REQUEST = "Bad Request please check mentioned field values once";
    static final String INVALID_CAPACITY_ERROR = "totalCapacity value is not valid";
	static final String INVALID_CAPACITY_HINT = "It should be greater than or equal to sum of all vehicles slot";
	static final String SLOTS_ALREADY_INITIALIZED = "Parking Slots already initialized";
	static final String INITILIZATION_DONE_BEFORE = "initialization already done";
	static final String SLOT_NOT_INITIALIZED = "Slot is not allocated yet";
	static final String POLICY_NOT_FOUND = "Policy is not found";
}
