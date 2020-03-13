package com.example.parking.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.VehicleDetails;
import com.example.parking.exception.AllReadyInitializedException;
import com.example.parking.exception.InvalidCapacityException;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.PricingPolicy;
import com.example.parking.service.ParkingTollService;

@RestController
@RequestMapping("/api")
public class ParkingTollController {
	@Autowired
	private ParkingTollService parkingTollService;

	private static boolean isInitialized;

	@PostMapping("/initialize")
	public ResponseEntity<ParkingInitializer> initializeTollParking(@RequestBody ParkingInitializer parkingSlotConfig)
			throws InvalidCapacityException, AllReadyInitializedException {
		if (!isInitialized) {
			if (!validParkingCapacity(parkingSlotConfig)) {
				throw new InvalidCapacityException(parkingSlotConfig.getTotalCapacity());
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

	@PostMapping("/enterparking")
	public ResponseEntity<ParkingSlotDto> getParkingSlot(@RequestBody VehicleDetails vehicleDetails) throws Exception {
		Optional<ParkingSlotDto> parkingSlot = parkingTollService.getAvailableParkingSlot(vehicleDetails);

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

	private List<String> getAllTypes(@Value("${VehicleTypes}") String[] carTypes) {
		return new ArrayList<>(Arrays.asList(carTypes));
	}

	private boolean validParkingCapacity(ParkingInitializer parkingSlotConfig) {
		if ((parkingSlotConfig.geteCar20KWSlotSize() + parkingSlotConfig.geteCar50KWSlotSize()
				+ parkingSlotConfig.getStdCarSlotSize()) > parkingSlotConfig.getTotalCapacity()) {
			return false;
		}
		return true;
	}
}
