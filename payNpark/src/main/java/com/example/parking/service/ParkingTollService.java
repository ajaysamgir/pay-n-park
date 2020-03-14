package com.example.parking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.ParkingSlotType;
import com.example.parking.dto.PolicyDetails;
import com.example.parking.exception.CarEntryAllreayExistException;
import com.example.parking.exception.SlotNotFoundException;
import com.example.parking.model.ParkingBill;

@Service
public interface ParkingTollService {

	Optional<ParkingSlotDto> getAvailableParkingSlot(CarDetails carDetails) throws SlotNotFoundException, CarEntryAllreayExistException;

	ParkingSlotType retrieveParkingSlotType(String vehicleNo);

	ParkingInitializer initialize(ParkingInitializer tollParkingConfig);

	Optional<ParkingBill> leaveParking(String plateNumber);

	boolean updatePricingPolicy(double fixedRate, double hourlyRate);

	Optional<List<ParkingSlotDto>> getAllParkingSlots();

	Optional<ParkingSlotDto> applyPolicy(PolicyDetails policyDetails) throws SlotNotFoundException;
}
