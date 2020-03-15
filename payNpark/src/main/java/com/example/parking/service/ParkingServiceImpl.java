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
import com.example.parking.exception.CarNotFoundInSlotException;
import com.example.parking.exception.ErrorMessages;
import com.example.parking.exception.InvalidCarTypeException;
import com.example.parking.exception.SlotNotFoundException;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.ParkingSlot;
import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;

public class ParkingServiceImpl implements ParkingService {

	private ParkingSlotRepository parkingSlotRepository;

	private ParkingBillRepository parkingBillRepository;

	public ParkingServiceImpl(ParkingSlotRepository parkingSlotRepository,
			ParkingBillRepository parkingBillRepository) {
		this.parkingSlotRepository = parkingSlotRepository;
		this.parkingBillRepository = parkingBillRepository;
	}

	@Override
	public Optional<ParkingSlotDto> getAvailableParkingSlot(CarDetails carDetails)
			throws SlotNotFoundException, CarEntryAllreayExistException, InvalidCarTypeException {
		if (validateExistingEntry(carDetails.getCarNumber())) {
			throw new CarEntryAllreayExistException(ErrorMessages.CAR_ENTRY_EXIST + ":" + carDetails.getCarNumber());
		}

		synchronized (ParkingServiceImpl.class) {
			Optional<ParkingSlot> firstParkingSlot = parkingSlotRepository.findAll().stream()
					.filter(ps -> ps.getParkingSlotType().equalsIgnoreCase(carDetails.getCarType()) && ps.isFree())
					.findFirst();
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
		if (availableEntry.isPresent()) {
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
		
			int countOfStandardCarSlot = tollParkingInitializer.getStandardCar();
			int countOfE20KWCarSlot = tollParkingInitializer.getElectric20KWCar();
			int countOfE50KWCarSlot = tollParkingInitializer.getElectric50KWCar();
			String defaultPolicy = tollParkingInitializer.getPolicy();
			double rentPerHour = tollParkingInitializer.getRentPerHour();
			double fixedRateAmt = tollParkingInitializer.getFixedRateAmount();

			IntStream.rangeClosed(1, countOfStandardCarSlot).forEach(i -> parkingSlotRepository
					.save(new ParkingSlot("Standard", true, defaultPolicy, rentPerHour, fixedRateAmt)));
			IntStream.rangeClosed(1, countOfE20KWCarSlot).forEach(i -> parkingSlotRepository
					.save(new ParkingSlot("Electric20KW", true, defaultPolicy, rentPerHour, fixedRateAmt)));
			IntStream.rangeClosed(1, countOfE50KWCarSlot).forEach(i -> parkingSlotRepository
					.save(new ParkingSlot("Electric50KW", true, defaultPolicy, rentPerHour, fixedRateAmt)));

			return tollParkingInitializer;
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

	@Override
	public synchronized Optional<ParkingBill> leaveParking(String carNumber) throws CarNotFoundInSlotException {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findByParkedCar(carNumber);
		if (parkingSlot.isPresent()) {
			if (parkingSlot.get().isFree()) {
				throw new CarNotFoundInSlotException(ErrorMessages.CAR_NUMBER_NOT_FOUND_IN_SLOT);
			}
			ParkingBill bill = new ParkingBill();
			bill.setCarNumber(carNumber);
			bill.setStartTime(parkingSlot.get().getParkingTime());
			bill.setEndTime(LocalDateTime.now());
			parkingSlotRepository.findById(parkingSlot.get().getId()).get().setFree(true);
			bill = generateParkingBill(bill, parkingSlot.get().getPolicy());
			parkingSlotRepository.save(parkingSlot.get());
			parkingBillRepository.save(bill);
			return Optional.of(bill);
		}
		throw new CarNotFoundInSlotException(ErrorMessages.CAR_NUMBER_NOT_FOUND_IN_SLOT);
	}

	@Override
	public Optional<ParkingSlotDto> applyPolicy(PolicyDetails policyDetails) throws SlotNotFoundException {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findAll().stream()
				.filter(p -> p.getId() == policyDetails.getParkingSlotNo()).findFirst();
		if (parkingSlot.isPresent()) {
			parkingSlot.get().setPolicy(policyDetails.getPolicyType());
			parkingSlotRepository.save(parkingSlot.get());
			return Optional.of(ParkingSlotDto.fromDomain(parkingSlot.get()));
		}
		throw new SlotNotFoundException(ErrorMessages.SLOT_IS_NOT_FOUND);
	}

	private ParkingBill generateParkingBill(ParkingBill parkingBill, String policy) {
		switch (policy) {
		case "Fixed":
			double amt = 10 * parkingHours(parkingBill.getStartTime(), parkingBill.getEndTime());
			parkingBill.setTotalBill(amt);
			break;
		case "Hourly":
			double amount = 10 * parkingHours(parkingBill.getStartTime(), parkingBill.getEndTime());
			parkingBill.setTotalBill(amount);
			break;
		default:
			parkingBill.setTotalBill(0.0);
		}
		return parkingBill;
	}

	private double parkingHours(LocalDateTime start, LocalDateTime end) {
		return start.until(end, ChronoUnit.MINUTES) / 60;
	}
}