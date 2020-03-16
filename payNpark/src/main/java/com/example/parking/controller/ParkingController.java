package com.example.parking.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingBillDto;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.PolicyDetails;
import com.example.parking.exception.AllReadyInitializedException;
import com.example.parking.exception.AppNotInitializedException;
import com.example.parking.exception.ErrorMessages;
import com.example.parking.exception.InvalidCapacityException;
import com.example.parking.exception.PolicyIsNoFoundException;
import com.example.parking.exception.SlotsNotInitializedException;
import com.example.parking.service.ParkingService;

@RestController
@RequestMapping("/api")
public class ParkingController {
	@Autowired
	private ParkingService parkingTollService;

	@Value("${parking.policies}")
	private String[] policies;

	@Value("${parking.slotTypes}")
	private String[] slotTypes;

	private boolean isInitialized;

	private static final Logger logger = LoggerFactory.getLogger(ParkingController.class);

	@PostMapping("/initialize")
	public ResponseEntity<ParkingInitializer> initializeTollParking(@RequestBody ParkingInitializer parkingSlotConfig)
			throws InvalidCapacityException, AllReadyInitializedException, PolicyIsNoFoundException {
		if (!isInitialized) {
			if (!validParkingCapacity(parkingSlotConfig)) {
				logger.error("API - api/initialize " + ErrorMessages.INVALID_CAPACITY_ERROR + ", "
						+ ErrorMessages.INVALID_CAPACITY_HINT);
				throw new InvalidCapacityException(parkingSlotConfig.getTotalCapacity());
			}

			if (!policyMatch(parkingSlotConfig.getPolicy())) {
				logger.error("API - api/initialize " + ErrorMessages.POLICY_NOT_FOUND);
				throw new PolicyIsNoFoundException();
			}
			Optional<ParkingInitializer> response = Optional
					.ofNullable(parkingTollService.initialize(parkingSlotConfig));

			if (response.isPresent()) {
				isInitialized = true;
				logger.info("Application initialization done successfully!");
				return ResponseEntity.ok(response.get());
			}
			return ResponseEntity.notFound().build();

		} else {
			logger.error("API - api/initialize " + ErrorMessages.SLOTS_ALREADY_INITIALIZED);
			throw new AllReadyInitializedException();
		}
	}

	@GetMapping("/slots")
	public ResponseEntity<List<ParkingSlotDto>> getParkingSlotDetail() throws SlotsNotInitializedException {
		Optional<List<ParkingSlotDto>> response = parkingTollService.getAllParkingSlots();
		if (response.isEmpty()) {
			logger.error("API - api/slots " + ErrorMessages.SLOT_NOT_INITIALIZED);
			throw new SlotsNotInitializedException();
		}
		logger.info("Get all parking slot list successfully!");
		return ResponseEntity.ok(response.get());
	}

	@PostMapping("/applypolicy")
	public ResponseEntity<ParkingSlotDto> applyPolicyOnParkingSlot(@RequestBody PolicyDetails policyDetails)
			throws Exception {

		if (isInitialized) {
			if (!validatePolicy(policyDetails.getPolicyType())) {
				logger.error("API - api/applypolicy " + ErrorMessages.POLICY_NOT_FOUND);
				throw new PolicyIsNoFoundException();
			}
			Optional<ParkingSlotDto> parkingSlot = parkingTollService.applyPolicy(policyDetails);

			if (parkingSlot.isPresent()) {
				logger.info("Policy applied successfully!");
				return ResponseEntity.ok(parkingSlot.get());
			}
			return ResponseEntity.notFound().build();
		}
		logger.error("API - api/applypolicy " + ErrorMessages.APP_NOT_INITIATED);
		throw new AppNotInitializedException(ErrorMessages.APP_NOT_INITIATED);
	}

	@PostMapping("/entry")
	public ResponseEntity<ParkingSlotDto> getParkingSlot(@Valid @RequestBody CarDetails carDetails) throws Exception {
		if (isInitialized) {
			Optional<ParkingSlotDto> parkingSlot = parkingTollService.getAvailableParkingSlot(carDetails);

			if (parkingSlot.isPresent()) {
				logger.info(
						"Parking slot allocated Successfully!" + " Car Number: " + parkingSlot.get().getParkedCar());
				return ResponseEntity.ok(parkingSlot.get());
			}
			return ResponseEntity.notFound().build();
		}
		logger.error("API - api/entry " + ErrorMessages.APP_NOT_INITIATED);
		throw new AppNotInitializedException(ErrorMessages.APP_NOT_INITIATED);
	}

	@GetMapping("/exit/{carNumber}")
	public ResponseEntity<ParkingBillDto> leaveParking(@PathVariable("carNumber") String carNumber) throws Exception {
		if (isInitialized) {
			Optional<ParkingBillDto> parkingBillResponse = parkingTollService.leaveParking(carNumber);
			if (parkingBillResponse.isPresent()) {
				logger.info(
						"Exit process done successfully!" + " Car Number: " + carNumber);
				return ResponseEntity.ok(parkingBillResponse.get());
			}
			return ResponseEntity.notFound().build();
		}
		logger.error("API - api/exit " + ErrorMessages.APP_NOT_INITIATED);
		throw new AppNotInitializedException(ErrorMessages.APP_NOT_INITIATED);
	}

	private boolean validParkingCapacity(ParkingInitializer parkingSlotConfig) {
		if ((parkingSlotConfig.getElectric20KWCar() + parkingSlotConfig.getElectric50KWCar()
				+ parkingSlotConfig.getStandardCar()) > parkingSlotConfig.getTotalCapacity()) {
			return false;
		}
		return true;
	}

	private boolean validatePolicy(String policyType) {
		for (String policy : policies) {
			if (policy.equalsIgnoreCase(policyType)) {
				return true;
			}
		}
		return false;
	}

	private boolean policyMatch(String policy) {
		for (String p : policies) {
			if (p.equalsIgnoreCase(policy)) {
				return true;
			}
		}
		return false;
	}
}
