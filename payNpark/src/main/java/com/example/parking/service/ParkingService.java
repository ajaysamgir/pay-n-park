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
import com.example.parking.exception.CarNotFoundInSlotException;
import com.example.parking.exception.InvalidCarTypeException;
import com.example.parking.exception.PolicyIsNoFoundException;
import com.example.parking.exception.SlotNotFoundException;
import com.example.parking.model.ParkingBill;

@Service
public interface ParkingService {

	Optional<ParkingSlotDto> getAvailableParkingSlot(CarDetails carDetails)
			throws SlotNotFoundException, CarEntryAllreayExistException, InvalidCarTypeException;

	ParkingInitializer initialize(ParkingInitializer tollParkingConfig);

	Optional<ParkingBill> leaveParking(String plateNumber) throws CarNotFoundInSlotException, PolicyIsNoFoundException;

	Optional<List<ParkingSlotDto>> getAllParkingSlots();

	Optional<ParkingSlotDto> applyPolicy(PolicyDetails policyDetails) throws SlotNotFoundException;
}