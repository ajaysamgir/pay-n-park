package com.example.parking.service;

import java.security.Policy;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import com.example.parking.config.TollParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.ParkingSlotType;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.ParkingSlot;
import com.example.parking.model.PricingPolicy;
import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;

public class ParkingTollServiceImpl implements ParkingTollService {

	private ParkingSlotRepository parkingSlotRepository;

	private ParkingBillRepository parkingBillRepository;

	private boolean initialized = false;

	private PricingPolicy pricingPolicy;

	public ParkingTollServiceImpl(ParkingSlotRepository parkingSlotRepository,
			ParkingBillRepository parkingBillRepository) {
		this.parkingSlotRepository = parkingSlotRepository;
		this.parkingBillRepository = parkingBillRepository;
	}

	@Override
	public Optional<ParkingSlotDto> getAvailableParkingSlot(String vehicleNo) {
		ParkingSlotType parkingSlotType = retrieveParkingSlotType(vehicleNo);
		synchronized (ParkingTollServiceImpl.class) {
			Optional<ParkingSlot> firstParkingSlot = parkingSlotRepository.findAll().stream()
					.filter(ps -> ps.getParkingSlotType().equals(parkingSlotType) && ps.isFree()).findFirst();
			if (firstParkingSlot.isPresent()) {
				ParkingSlot parkingSlot = firstParkingSlot.get();
				parkingSlot.setFree(false);
				parkingSlotRepository.save(parkingSlot);
				return Optional.of(ParkingSlotDto.fromDomain(firstParkingSlot.get()));
			}

			return Optional.empty();
		}
	}

	public ParkingSlotType retrieveParkingSlotType(String vehicleNo) {
		Random random = new Random();
		return ParkingSlotType.values()[random.nextInt(ParkingSlotType.values().length)];

	}

	@Override
	public TollParkingInitializer initialize(TollParkingInitializer tollParkingInitializer) {
		if (!initialized) {
			int numberOfStandardParkingSlot = tollParkingInitializer.getElectricCarStandardParkingSlotSize();
			int numberOfElectricCar20KWParkingSlot = tollParkingInitializer.getElectricCar20KWParkingSlotSize();
			int numberOfElectricCar50KWParkingSlot = tollParkingInitializer.getElectricCar50KWParkingSlotSize();
			IntStream.rangeClosed(1, numberOfStandardParkingSlot)
					.forEach(i -> parkingSlotRepository.save(new ParkingSlot(ParkingSlotType.STANDARD, true)));
			IntStream.rangeClosed(1, numberOfElectricCar20KWParkingSlot)
					.forEach(i -> parkingSlotRepository.save(new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_20KW, true)));
			IntStream.rangeClosed(1, numberOfElectricCar50KWParkingSlot)
					.forEach(i -> parkingSlotRepository.save(new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, true)));

			updatePricingPolicy(tollParkingInitializer.getFixedAmount(), tollParkingInitializer.getHourPrice());
			initialized = true;

			return tollParkingInitializer;
		}

		return null;
	}

	public synchronized Optional<ParkingBill> leaveParking(String vehicleNo) {
		Optional<ParkingBill> firstParkingBill = parkingBillRepository.findAll().stream()
				.filter(pb -> pb.getVehicleNo().equals(vehicleNo) && pb.getEndTime() == null).findFirst();
		if (firstParkingBill.isPresent()) {
			ParkingBill parkingBill = firstParkingBill.get();
			parkingBill.setEndTime(LocalDateTime.now());
			parkingSlotRepository.findById(parkingBill.getParkingSlot().getId()).get().setFree(true);
			handleParkingBill(parkingBill);
		}

		return firstParkingBill;
	}

	public boolean updatePricingPolicy(double fixAmt, double hourRate) {
		if (fixAmt >= 0 && hourRate > 0) {
			PricingPolicy policy = new PricingPolicy(fixAmt, hourRate);
			this.pricingPolicy = policy;
			return true;
		}

		return false;
	}

	public ParkingBill handleParkingBill(ParkingBill parkingBill) {
		double price = pricingPolicy.getFixedAmount() + pricingPolicy.getHourPrice() * parkingHours(parkingBill);
		parkingBill.setPrice(price);

		return parkingBillRepository.save(parkingBill);
	}

	private double parkingHours(ParkingBill parkingBill) {
		return parkingBill.getStartTime().until(parkingBill.getEndTime(), ChronoUnit.HOURS);
	}

}
