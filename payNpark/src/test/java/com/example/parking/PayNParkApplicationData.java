package com.example.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.parking.dto.CarDetails;
import com.example.parking.model.ParkingSlot;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.PolicyDetails;

public class PayNParkApplicationData {

	public static ParkingSlot getParkingSlotData() {
		return new ParkingSlot("Standard", true, "fixed", 10, 10, LocalDateTime.now());
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

	public static List<ParkingSlotDto> getParkingSlotDataList() {
		ParkingSlotDto parkingSlot = ParkingSlotDto.fromDomain(PayNParkApplicationData.getParkingSlotData());
		List<ParkingSlotDto> slots = new ArrayList<>();
		slots.add(parkingSlot);
		return slots;
	}

}
