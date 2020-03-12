package com.example.parking.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.parking.config.TollParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.ParkingSlotType;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.PricingPolicy;

@Service
public interface ParkingTollService {

	Optional<ParkingSlotDto> getAvailableParkingSlot(String vehicleNo);
	
	ParkingSlotType retrieveParkingSlotType(String vehicleNo);

	TollParkingInitializer initialize(TollParkingInitializer tollParkingConfig);
	
	Optional<ParkingBill> leaveParking(String plateNumber);

	boolean updatePricingPolicy(double fixedRate, double hourlyRate);
}
