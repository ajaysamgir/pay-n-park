package com.example.parking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.ParkingSlotType;
import com.example.parking.dto.VehicleDetails;
import com.example.parking.model.ParkingBill;

@Service
public interface ParkingTollService {

	Optional<ParkingSlotDto> getAvailableParkingSlot(VehicleDetails vehicleDetails);

	ParkingSlotType retrieveParkingSlotType(String vehicleNo);

	ParkingInitializer initialize(ParkingInitializer tollParkingConfig);

	Optional<ParkingBill> leaveParking(String plateNumber);

	boolean updatePricingPolicy(double fixedRate, double hourlyRate);

	Optional<List<ParkingSlotDto>> getAllParkingSlots();
}
