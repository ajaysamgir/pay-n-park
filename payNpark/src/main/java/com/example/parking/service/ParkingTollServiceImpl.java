package com.example.parking.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.ParkingSlotType;
import com.example.parking.dto.PolicyDetails;
import com.example.parking.exception.CarEntryAllreayExistException;
import com.example.parking.exception.ErrorMessages;
import com.example.parking.exception.SlotNotFoundException;
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
	public Optional<ParkingSlotDto> getAvailableParkingSlot(CarDetails carDetails) throws SlotNotFoundException, CarEntryAllreayExistException {
		String slotType = carDetails.getCarType();
		
		if(validateExistingEntry(carDetails.getCarNumber())) {
			throw new CarEntryAllreayExistException(ErrorMessages.CAR_ENTRY_EXIST + ":" + carDetails.getCarNumber());
		}
		
		synchronized (ParkingTollServiceImpl.class) {
			Optional<ParkingSlot> firstParkingSlot = parkingSlotRepository.findAll().stream()
					.filter(ps -> ps.getParkingSlotType().equalsIgnoreCase(slotType) && ps.isFree()).findFirst();
			if (firstParkingSlot.isPresent()) {
				ParkingSlot parkingSlot = firstParkingSlot.get();
				parkingSlot.setFree(false);
				parkingSlot.setParkedCar(carDetails.getCarNumber());
				parkingSlotRepository.save(parkingSlot);
				return Optional.of(ParkingSlotDto.fromDomain(firstParkingSlot.get()));
			}
			throw new SlotNotFoundException(ErrorMessages.SLOT_IS_FULL + " or " + ErrorMessages.APP_NOT_INITIATED);
		}
	}

	private boolean validateExistingEntry(String carNumber) {
		Optional<ParkingSlot> availableEntry = parkingSlotRepository.findByParkedCar(carNumber);
		if(availableEntry.isPresent()) {
			return true;
		}
		return false;
	}

	public ParkingSlotType retrieveParkingSlotType(String vehicleNo) {
		Random random = new Random();
		return ParkingSlotType.values()[random.nextInt(ParkingSlotType.values().length)];
	}

	@Override
	public ParkingInitializer initialize(ParkingInitializer tollParkingInitializer) {
		if (!initialized) {

			int countOfStandardCarSlot = tollParkingInitializer.getStandardCar();
			int countOfE20KWCarSlot = tollParkingInitializer.getElectric20KWCar();
			int countOfE50KWCarSlot = tollParkingInitializer.getElectric50KWCar();
			String defaultPolicy = tollParkingInitializer.getPolicy();
			
			IntStream.rangeClosed(1, countOfStandardCarSlot)
					.forEach(i -> parkingSlotRepository.save(new ParkingSlot("Standard", true, defaultPolicy)));
			IntStream.rangeClosed(1, countOfE20KWCarSlot)
					.forEach(i -> parkingSlotRepository.save(new ParkingSlot("Electric20KW", true, defaultPolicy)));
			IntStream.rangeClosed(1, countOfE50KWCarSlot)
					.forEach(i -> parkingSlotRepository.save(new ParkingSlot("Electric50KW", true, defaultPolicy)));

			return tollParkingInitializer;
		}

		return null;
	}

	@Override
	public Optional<List<ParkingSlotDto>> getAllParkingSlots() {
		List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
		if (!parkingSlots.isEmpty()) {
			List<ParkingSlotDto> listOfParkingSlots = new ArrayList<ParkingSlotDto>();
			parkingSlots.forEach(parkingSlot -> listOfParkingSlots.add(ParkingSlotDto.fromDomain(parkingSlot)));
			return Optional.of(listOfParkingSlots);
		}

		return Optional.empty();
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

	@Override
	public Optional<ParkingSlotDto> applyPolicy(PolicyDetails policyDetails) throws SlotNotFoundException {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findAll().stream()
				.filter(p -> p.getId() == policyDetails.getParkingSlotNo()).findFirst();
		if(parkingSlot.isPresent()) {
			parkingSlot.get().setPolicy(policyDetails.getPolicyType());
			parkingSlotRepository.save(parkingSlot.get());
			return Optional.of(ParkingSlotDto.fromDomain(parkingSlot.get()));
		}
		throw new SlotNotFoundException(ErrorMessages.SLOT_IS_NOT_FOUND);
	}
}
