package com.example.parking;

import com.example.parking.dto.CarDetails;
import com.example.parking.model.ParkingSlot;

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

}
