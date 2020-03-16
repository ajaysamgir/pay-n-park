package com.example.parking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingBillDto;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.PolicyDetails;
import com.example.parking.exception.CarEntryAllreayExistException;
import com.example.parking.exception.CarNotFoundInSlotException;
import com.example.parking.exception.ErrorMessages;
import com.example.parking.exception.InvalidCarTypeException;
import com.example.parking.exception.PolicyIsNoFoundException;
import com.example.parking.exception.SlotNotFoundException;
import com.example.parking.model.ParkingBill;
import com.example.parking.model.ParkingSlot;
import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;

public class ParkingServiceImpl implements ParkingService {

	private ParkingSlotRepository parkingSlotRepository;

	private ParkingBillRepository parkingBillRepository;

	private static final Logger logger = LoggerFactory.getLogger(ParkingServiceImpl.class);

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

				logger.info("Parking slot allocated sucessfully to Slot No = " + parkingSlot.getId() + " Car No = "
						+ carDetails.getCarNumber());
				return Optional.of(ParkingSlotDto.fromDomain(firstParkingSlot.get()));
			}
			logger.error(ErrorMessages.SLOT_IS_FULL);
			throw new SlotNotFoundException(ErrorMessages.SLOT_IS_FULL);
		}
	}

	@Override
	public ParkingInitializer initialize(ParkingInitializer tollParkingInitializer) {

		int countOfStandardCarSlot = tollParkingInitializer.getStandardCar();
		int countOfE20KWCarSlot = tollParkingInitializer.getElectric20KWCar();
		int countOfE50KWCarSlot = tollParkingInitializer.getElectric50KWCar();
		String defaultPolicy = tollParkingInitializer.getPolicy();
		double rentPerHour = tollParkingInitializer.getRentPerHour();
		double fixedRateAmt = tollParkingInitializer.getFixedRateAmount();

		IntStream.rangeClosed(1, countOfStandardCarSlot).forEach(i -> parkingSlotRepository.save(
				new ParkingSlot("Standard", true, defaultPolicy, rentPerHour, fixedRateAmt, LocalDateTime.now())));
		IntStream.rangeClosed(1, countOfE20KWCarSlot).forEach(i -> parkingSlotRepository.save(
				new ParkingSlot("Electric20KW", true, defaultPolicy, rentPerHour, fixedRateAmt, LocalDateTime.now())));
		IntStream.rangeClosed(1, countOfE50KWCarSlot).forEach(i -> parkingSlotRepository.save(
				new ParkingSlot("Electric50KW", true, defaultPolicy, rentPerHour, fixedRateAmt, LocalDateTime.now())));
		logger.info("Parking slots initialized successfully!");
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

		logger.info("Allocated slots not available");
		return Optional.empty();
	}

	@Override
	public Optional<ParkingBillDto> leaveParking(String carNumber)
			throws CarNotFoundInSlotException, PolicyIsNoFoundException {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findByParkedCar(carNumber);
		synchronized (ParkingServiceImpl.class) {
			if (parkingSlot.isPresent()) {
				if (parkingSlot.get().isFree()) {
					logger.error(ErrorMessages.CAR_NOT_FOUND_IN_SLOT + " : " + carNumber);
					throw new CarNotFoundInSlotException(ErrorMessages.CAR_NOT_FOUND_IN_SLOT);
				}
				ParkingBill bill = new ParkingBill();
				bill.setCarNumber(carNumber);
				bill.setStartTime(parkingSlot.get().getParkingTime());
				bill.setEndTime(LocalDateTime.now());
				bill = generateParkingBill(bill, parkingSlot.get());

				parkingSlot.get().setFree(true);
				parkingSlot.get().setParkedCar(null);
				parkingSlotRepository.save(parkingSlot.get());

				bill.setParkingSlot(parkingSlot.get());
				parkingBillRepository.save(bill);

				logger.info("Exit process done, parking slot marked as free and total bill of vehicle : " + carNumber
						+ " is generated");
				return Optional.of(ParkingBillDto.fromDomain(bill));
			}

		}
		logger.error(ErrorMessages.CAR_NOT_FOUND_IN_SLOT);
		throw new CarNotFoundInSlotException(ErrorMessages.CAR_NOT_FOUND_IN_SLOT);
	}

	@Override
	public Optional<ParkingSlotDto> applyPolicy(PolicyDetails policyDetails) throws SlotNotFoundException, PolicyIsNoFoundException {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findAll().stream()
				.filter(p -> p.getId() == policyDetails.getParkingSlotNo()).findFirst();
		if (parkingSlot.isPresent()) {
			if (policyDetails.getPolicyType().equalsIgnoreCase(AppConstants.FIXED)
					|| policyDetails.getPolicyType().equalsIgnoreCase(AppConstants.HOURLY)) {
				parkingSlot.get().setPolicy(policyDetails.getPolicyType());
				parkingSlotRepository.save(parkingSlot.get());

				logger.info("Policy updated successfully!");
				return Optional.of(ParkingSlotDto.fromDomain(parkingSlot.get()));
			} else {
				throw new PolicyIsNoFoundException();
			}
		}
		logger.error(ErrorMessages.SLOT_IS_NOT_FOUND + " : Update Policy");
		throw new SlotNotFoundException(ErrorMessages.SLOT_IS_NOT_FOUND);
	}

	private ParkingBill generateParkingBill(ParkingBill parkingBill, ParkingSlot parkingSlot)
			throws PolicyIsNoFoundException {
		if (parkingSlot.getPolicy().equalsIgnoreCase(AppConstants.FIXED)) {
			double amt = parkingSlot.getFixedAmount()
					* (parkingHours(parkingBill.getStartTime(), parkingBill.getEndTime()));
			parkingBill.setTotalBill(amt);
		} else if (parkingSlot.getPolicy().equalsIgnoreCase(AppConstants.HOURLY)) {
			double amt = parkingSlot.getRentPerHour()
					* parkingHours(parkingBill.getStartTime(), parkingBill.getEndTime());
			parkingBill.setTotalBill(amt);
		} else {
			logger.error(ErrorMessages.POLICY_NOT_FOUND);
			throw new PolicyIsNoFoundException();
		}
		return parkingBill;
	}

	private double parkingHours(LocalDateTime start, LocalDateTime end) {
		double count = end.getHour() - start.getHour();
		return count == 0 ? 1 : count;
	}

	private boolean validateExistingEntry(String carNumber) {
		Optional<ParkingSlot> availableEntry = parkingSlotRepository.findByParkedCar(carNumber);
		if (availableEntry.isPresent()) {
			return true;
		}
		return false;
	}
}