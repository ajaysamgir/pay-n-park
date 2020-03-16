package com.example.parking.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.parking.PayNParkApplicationData;
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
import com.example.parking.model.ParkingSlot;
import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;
import com.example.parking.service.AppConstants;
import com.example.parking.service.ParkingService;
import com.example.parking.service.ParkingServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParkingServiceImpl.class)
public class ParkingServiceTest {

	@MockBean
	private ParkingSlotRepository parkingSlotRepository;

	@MockBean
	private ParkingBillRepository parkingBillRepository;

	private ParkingService service;

	@BeforeEach
	public void setup() {
		service = new ParkingServiceImpl(parkingSlotRepository, parkingBillRepository);
	}

	/**
	 * Testcases for initialize parkingslots
	 */
	@Test
	public void initializeParkingSlotsSuccessTest() {
		ParkingInitializer initializer = PayNParkApplicationData.getParkingInitializer();
		ParkingInitializer result = service.initialize(initializer);

		assertEquals(initializer, result);
		assertEquals(10, result.getElectric20KWCar());
		assertEquals(10, result.getElectric50KWCar());
		assertEquals(10, result.getStandardCar());
		assertEquals("fixed", result.getPolicy());
	}

	/**
	 * Get all parking slots
	 */
	@Test
	public void getAllParkingSlots() {
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		when(parkingSlotRepository.findAll()).thenReturn(Arrays.asList(parkingSlot));

		Optional<List<ParkingSlotDto>> result = service.getAllParkingSlots();

		assertFalse(result.isEmpty());
		assertEquals(1, parkingSlot.getId());
		assertEquals(10.0, parkingSlot.getRentPerHour());
		assertEquals(10.0, parkingSlot.getFixedAmount());
		assertEquals(null, parkingSlot.getParkedCar());
		assertEquals(true, parkingSlot.getFree());
	}

	@Test
	public void getAllParkingSlotsReturnEmpty() {
		when(parkingSlotRepository.findAll()).thenReturn(new ArrayList<>());

		Optional<List<ParkingSlotDto>> result = service.getAllParkingSlots();

		assertTrue(result.isEmpty());
	}

	/**
	 * Testcases for Car entry in parking
	 */
	@Test
	public void getParkingSlotSuccessTest()
			throws SlotNotFoundException, CarEntryAllreayExistException, InvalidCarTypeException {
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		CarDetails carDetails = PayNParkApplicationData.getCarDetailsData();
		when(parkingSlotRepository.findAll()).thenReturn(Arrays.asList(parkingSlot));

		Optional<ParkingSlotDto> parkingSlotDto = service.getAvailableParkingSlot(carDetails);
		assertTrue(parkingSlotDto.isPresent());
		assertTrue(parkingSlotDto.get() instanceof ParkingSlotDto);
	}

	@Test
	public void getParkingSlotWithSlotNotFoundExceptionTest()
			throws SlotNotFoundException, CarEntryAllreayExistException, InvalidCarTypeException {
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		CarDetails carDetails = PayNParkApplicationData.getCarDetailsData();
		parkingSlot.setId(1L);
		carDetails.setCarType("SomethingElse");
		when(parkingSlotRepository.findAll()).thenReturn(new ArrayList<>());

		assertThrows(SlotNotFoundException.class, () -> service.getAvailableParkingSlot(carDetails));
	}

	@Test
	public void getParkingSlotWithDuplicateCarEntryTest()
			throws SlotNotFoundException, CarEntryAllreayExistException, InvalidCarTypeException {
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		CarDetails carDetails = PayNParkApplicationData.getCarDetailsData();
		parkingSlot.setId(1L);
		when(parkingSlotRepository.findByParkedCar(carDetails.getCarNumber())).thenReturn(Optional.of(parkingSlot));

		assertThrows(CarEntryAllreayExistException.class, () -> service.getAvailableParkingSlot(carDetails),
				ErrorMessages.CAR_ENTRY_EXIST);
	}

	/**
	 * leaveParking
	 * 
	 * @throws CarNotFoundInSlotException
	 * @throws PolicyIsNoFoundException
	 */
	@Test
	public void leaveParkingSuccessTest() throws CarNotFoundInSlotException, PolicyIsNoFoundException {
		String carNumber = "MH12AD9415";
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		parkingSlot.setParkedCar(carNumber);
		parkingSlot.setParkingTime((LocalDateTime.now().minusHours(2)));
		parkingSlot.setFree(false);
		when(parkingSlotRepository.findByParkedCar(carNumber)).thenReturn(Optional.of(parkingSlot));

		Optional<ParkingBillDto> bill = service.leaveParking(carNumber);

		assertEquals("MH12AD9415", bill.get().getParkingSlot().getParkedCar());
		assertEquals(true, bill.get().getParkingSlot().getFree());
		assertNotNull(bill.get().getBillAmount());
	}

	@Test
	public void leaveParkingHourlyPolicySuccessTest() throws CarNotFoundInSlotException, PolicyIsNoFoundException {
		String carNumber = "MH12AD9415";
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		parkingSlot.setParkedCar(carNumber);
		parkingSlot.setParkingTime((LocalDateTime.now().minusHours(2)));
		parkingSlot.setFree(false);
		parkingSlot.setPolicy(AppConstants.HOURLY);
		when(parkingSlotRepository.findByParkedCar(carNumber)).thenReturn(Optional.of(parkingSlot));

		Optional<ParkingBillDto> bill = service.leaveParking(carNumber);

		assertEquals("MH12AD9415", bill.get().getParkingSlot().getParkedCar());
		assertEquals(true, bill.get().getParkingSlot().getFree());
		assertNotNull(bill.get().getBillAmount());
	}

	@Test
	public void leaveParkingUnknownPolicyTest() throws CarNotFoundInSlotException, PolicyIsNoFoundException {
		String carNumber = "MH12AD9415";
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		parkingSlot.setParkedCar(carNumber);
		parkingSlot.setParkingTime((LocalDateTime.now().minusHours(2)));
		parkingSlot.setFree(false);
		parkingSlot.setPolicy("SomethingElse");
		when(parkingSlotRepository.findByParkedCar(carNumber)).thenReturn(Optional.of(parkingSlot));

		assertThrows(PolicyIsNoFoundException.class, () -> service.leaveParking(carNumber));
	}

	@Test
	public void leaveParkingWithCarNotFoundInSlotExceptionTest() throws CarNotFoundInSlotException {
		String carNumber = "MH12AD9415";
		when(parkingSlotRepository.findByParkedCar(carNumber))
				.thenReturn(Optional.of(PayNParkApplicationData.getParkingSlotData()));

		assertThrows(CarNotFoundInSlotException.class, () -> service.leaveParking(carNumber));
	}

	@Test
	public void leaveParkingWithInvalidSlotTest() throws CarNotFoundInSlotException {
		String carNumber = "MH12AD9415";
		when(parkingSlotRepository.findByParkedCar(carNumber))
				.thenReturn(Optional.ofNullable(null));

		assertThrows(CarNotFoundInSlotException.class, () -> service.leaveParking(carNumber));
	}

	@Test
	public void leaveParkingWithPolicyNotFoundInSlotExceptionTest() throws CarNotFoundInSlotException {
		String carNumber = "MH12AD9415";
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setPolicy("SomethingElse");
		when(parkingSlotRepository.findByParkedCar(carNumber)).thenReturn(Optional.of(parkingSlot));

		assertThrows(CarNotFoundInSlotException.class, () -> service.leaveParking(carNumber));
	}

	/**
	 * Apply policy test
	 * 
	 * @throws SlotNotFoundException
	 * @throws PolicyIsNoFoundException 
	 */
	@Test
	public void applyPolicySuccessTest() throws SlotNotFoundException, PolicyIsNoFoundException {
		PolicyDetails policyDetails = PayNParkApplicationData.getPolicyDetailsData();
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		when(parkingSlotRepository.findAll()).thenReturn(Arrays.asList(parkingSlot));

		Optional<ParkingSlotDto> result = service.applyPolicy(policyDetails);
		
		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getId());
		assertEquals("Hourly", result.get().getPolicy());
	}

	@Test
	public void applyPolicySlotNotFoundExceptionTest() throws SlotNotFoundException {
		PolicyDetails policyDetails = PayNParkApplicationData.getPolicyDetailsData();
		policyDetails.setParkingSlotNo(3L);
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		when(parkingSlotRepository.findAll()).thenReturn(Arrays.asList(parkingSlot));

		assertThrows(SlotNotFoundException.class,() -> service.applyPolicy(policyDetails));
	}
	
	@Test
	public void applyPolicyWithPolicyIsNoFoundExceptionTest() throws SlotNotFoundException {
		PolicyDetails policyDetails = PayNParkApplicationData.getPolicyDetailsData();
		policyDetails.setParkingSlotNo(1L);
		policyDetails.setPolicyType("SomethingElse");
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();
		parkingSlot.setId(1L);
		when(parkingSlotRepository.findAll()).thenReturn(Arrays.asList(parkingSlot));

		assertThrows(PolicyIsNoFoundException.class,() -> service.applyPolicy(policyDetails));
	}
}
