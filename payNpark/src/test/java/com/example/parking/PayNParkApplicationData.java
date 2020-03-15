package com.example.parking;

import com.example.parking.dto.CarDetails;
import com.example.parking.model.ParkingSlot;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.PolicyDetails;

public class PayNParkApplicationData {

	public static ParkingSlot getParkingSlotData() {
		return new ParkingSlot("Standard", true, "fixed", 10, 10);
	}

	public static CarDetails getCarDetailsData() {
		CarDetails carDetails = new CarDetails();
		carDetails.setCarNumber("MH12AD9415");
		carDetails.setCarType("Standard");
		return carDetails;
	}

	public static ParkingInitializer getParkingInitializer() {
		return new ParkingInitializer(10, 10, 10, 30, 10.0, 15.0, "fixed");
	}

	public static PolicyDetails getPolicyDetailsData() {
		PolicyDetails policyDetails = new PolicyDetails();
		policyDetails.setParkingSlotNo(1L);
		policyDetails.setPolicyType("Hourly");
		policyDetails.setRate(15.0);
		return policyDetails;
	}

}
