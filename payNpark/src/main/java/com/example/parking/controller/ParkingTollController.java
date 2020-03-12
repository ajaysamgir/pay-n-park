package com.example.parking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.config.TollParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.PricingPolicy;
import com.example.parking.service.ParkingTollService;

@RestController
@RequestMapping("/api")
public class ParkingTollController {
	@Autowired
	private ParkingTollService parkingTollService;

	@PostMapping("/init")
	public ResponseEntity<TollParkingInitializer> initializeTollParking(
			@RequestBody TollParkingInitializer tollParkingConfig) throws Exception {
		Optional<TollParkingInitializer> response = Optional
				.ofNullable(parkingTollService.initialize(tollParkingConfig));

		if (response.isPresent()) {
			return ResponseEntity.ok(response.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/enterparking/{vehicleNo}")
	public ResponseEntity<ParkingSlotDto> getParkingSlot(@PathVariable("vehicleNo") String vehicleNo) throws Exception {
		Optional<ParkingSlotDto> parkingSlot = parkingTollService.getAvailableParkingSlot(vehicleNo);

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
}
