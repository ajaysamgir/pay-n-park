package com.example.parking.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.parking.PayNParkApplicationData;
import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.exception.CarEntryAllreayExistException;
import com.example.parking.exception.ErrorMessages;
import com.example.parking.exception.InvalidCarTypeException;
import com.example.parking.exception.SlotNotFoundException;
import com.example.parking.model.ParkingSlot;
import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;
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
		assertEquals(result.getElectric20KWCar(), 10);
		assertEquals(result.getElectric50KWCar(), 10);
		assertEquals(result.getStandardCar(), 10);
		assertEquals(result.getPolicy(), "fixed");
	}
	
	@Test
	public void initializeParkingSlotsWithInvalidCapacityTest() {
		ParkingInitializer initializer = PayNParkApplicationData.getParkingInitializer();
		ParkingInitializer result = service.initialize(initializer);
		
		assertEquals(initializer, result);
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

		assertThrows(CarEntryAllreayExistException.class, () -> service.getAvailableParkingSlot(carDetails), ErrorMessages.CAR_ENTRY_EXIST);
	}
}
