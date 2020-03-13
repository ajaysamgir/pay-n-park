package com.example.parking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.PolicyDetails;
import com.example.parking.exception.AllReadyInitializedException;
import com.example.parking.exception.PolicyIsNoFoundException;
import com.example.parking.exception.InvalidCapacityException;
import com.example.parking.exception.SlotsNotInitializedException;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.PricingPolicy;
import com.example.parking.service.ParkingTollService;

@RestController
@RequestMapping("/api")
public class ParkingTollController {
	@Autowired
	private ParkingTollService parkingTollService;

	@Value("${parking.slotTypes}")
	private String[] slotTypes;

	@Value("${parking.policies}")
	private String[] policies;

	private static boolean isInitialized;

	@PostMapping("/initialize")
	public ResponseEntity<ParkingInitializer> initializeTollParking(@RequestBody ParkingInitializer parkingSlotConfig)
			throws InvalidCapacityException, AllReadyInitializedException, PolicyIsNoFoundException {
		if (!isInitialized) {
			if (!validParkingCapacity(parkingSlotConfig)) {
				throw new InvalidCapacityException(parkingSlotConfig.getTotalCapacity());
			}

			if (!policyMatch(parkingSlotConfig.getPolicy())) {
				throw new PolicyIsNoFoundException();
			}
			Optional<ParkingInitializer> response = Optional
					.ofNullable(parkingTollService.initialize(parkingSlotConfig));

			if (response.isPresent()) {
				isInitialized = true;
				return ResponseEntity.ok(response.get());
			}
			return ResponseEntity.notFound().build();

		} else {
			throw new AllReadyInitializedException();
		}
	}

	private boolean policyMatch(String policy) {
		for (String p : policies) {
			if (p.equalsIgnoreCase(policy)) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/slots")
	public ResponseEntity<List<ParkingSlotDto>> getParkingSlotDetail() throws SlotsNotInitializedException {
		Optional<List<ParkingSlotDto>> response = parkingTollService.getAllParkingSlots();
		if (response.isEmpty()) {
			throw new SlotsNotInitializedException();
		}
		return ResponseEntity.ok(response.get());
	}

	@PostMapping("/applypolicy")
	public ResponseEntity<ParkingSlotDto> getParkingSlot(@RequestBody PolicyDetails policyDetails) throws Exception {

		Optional<ParkingSlotDto> parkingSlot = parkingTollService.applyPolicy(policyDetails);

		if (parkingSlot.isPresent()) {
			return ResponseEntity.ok(parkingSlot.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/entry")
	public ResponseEntity<ParkingSlotDto> getParkingSlot(@RequestBody CarDetails carDetails) throws Exception {

		Optional<ParkingSlotDto> parkingSlot = parkingTollService.getAvailableParkingSlot(carDetails);

		if (parkingSlot.isPresent()) {
			return ResponseEntity.ok(parkingSlot.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/pricingpolicy")
	public ResponseEntity<PricingPolicy> updatePricingPolicy(@RequestBody PricingPolicy pricingPolicy)
			throws Exception {
		if (parkingTollService.updatePricingPolicy(pricingPolicy.getFixedAmount(), pricingPolicy.getHourPrice())) {
			return ResponseEntity.ok(pricingPolicy);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/leaveparking/{vehicleNo}")
	public ResponseEntity<ParkingBill> leaveParking(@PathVariable("vehicleNo") String plateNumber) throws Exception {
		Optional<ParkingBill> parkingBillResponse = parkingTollService.leaveParking(plateNumber);
		if (parkingBillResponse.isPresent())
			return ResponseEntity.ok(parkingBillResponse.get());
		return ResponseEntity.notFound().build();
	}

	private boolean validParkingCapacity(ParkingInitializer parkingSlotConfig) {
		if ((parkingSlotConfig.getElectric20KWCar() + parkingSlotConfig.getElectric50KWCar()
				+ parkingSlotConfig.getStandardCar()) > parkingSlotConfig.getTotalCapacity()) {
			return false;
		}
		return true;
	}
}