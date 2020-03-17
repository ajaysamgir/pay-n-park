package com.example.parking.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.parking.PayNParkApplicationData;
import com.example.parking.controller.ParkingController;
import com.example.parking.dto.CarDetails;
import com.example.parking.dto.ParkingInitializer;
import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.exception.AllReadyInitializedException;
import com.example.parking.exception.InvalidCapacityException;
import com.example.parking.exception.PolicyIsNoFoundException;
import com.example.parking.exception.SlotsNotInitializedException;
import com.example.parking.service.ParkingServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = { "parking.policies = Fixed, Hourly" })
public class ParkingControllerTest {

	@Spy
	@InjectMocks
	private ParkingController parkingController;

	@Mock
	private ParkingServiceImpl parkingService;

	@BeforeEach
	public void setUp() {
		ReflectionTestUtils.setField(parkingController, "policies", new String[] { "Fixed", "Hourly" });
	}

	/**
	 * Testcases of initialization operation
	 * 
	 * @throws InvalidCapacityException
	 * @throws AllReadyInitializedException
	 * @throws PolicyIsNoFoundException
	 */
	@Test
	public void initApplicationTest()
			throws InvalidCapacityException, AllReadyInitializedException, PolicyIsNoFoundException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ParkingInitializer init = PayNParkApplicationData.getParkingInitializer();
		when(parkingService.initialize(init)).thenReturn(init);

		ResponseEntity<ParkingInitializer> responseEntity = parkingController.initializeTollParking(init);

		assertThat(responseEntity.getBody().getElectric20KWCar()).isEqualTo(10);
		assertThat(responseEntity.getBody().getElectric50KWCar()).isEqualTo(10);
		assertThat(responseEntity.getBody().getFixedRateAmount()).isEqualTo(15.0);
		assertThat(responseEntity.getBody().getRentPerHour()).isEqualTo(10.0);
		assertThat(responseEntity.getBody().getFixedRateAmount()).isEqualTo(15.0);
		assertThat(responseEntity.getBody().getPolicy()).isEqualTo("fixed");
		assertThat(responseEntity.getStatusCode().is2xxSuccessful());
	}

	@Test
	public void initApplicationWithInvalidCapacityTest()
			throws InvalidCapacityException, AllReadyInitializedException, PolicyIsNoFoundException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ParkingInitializer init = PayNParkApplicationData.getParkingInitializer();
		init.setTotalCapacity(20);

		assertThrows(InvalidCapacityException.class, () -> parkingController.initializeTollParking(init));
	}

	@Test
	public void initApplicationWithInvalidPolicyTest()
			throws InvalidCapacityException, AllReadyInitializedException, PolicyIsNoFoundException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ParkingInitializer init = PayNParkApplicationData.getParkingInitializer();
		init.setPolicy("SomethingElse");

		assertThrows(PolicyIsNoFoundException.class, () -> parkingController.initializeTollParking(init));
	}

	@Test
	public void initApplicationWithDuplicationTest()
			throws InvalidCapacityException, AllReadyInitializedException, PolicyIsNoFoundException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ParkingInitializer init = PayNParkApplicationData.getParkingInitializer();
		when(parkingService.initialize(init)).thenReturn(init);

		ResponseEntity<ParkingInitializer> responseEntity = parkingController.initializeTollParking(init);

		assertThat(responseEntity.getBody().getElectric20KWCar()).isEqualTo(10);
		assertThat(responseEntity.getBody().getElectric50KWCar()).isEqualTo(10);
		assertThat(responseEntity.getBody().getFixedRateAmount()).isEqualTo(15.0);
		assertThat(responseEntity.getBody().getRentPerHour()).isEqualTo(10.0);
		assertThat(responseEntity.getBody().getFixedRateAmount()).isEqualTo(15.0);
		assertThat(responseEntity.getBody().getPolicy()).isEqualTo("fixed");
		assertThat(responseEntity.getStatusCode().is2xxSuccessful());

		assertThrows(AllReadyInitializedException.class, () -> parkingController.initializeTollParking(init));
	}

	/**
	 * Get all slots
	 * 
	 * @throws Exception
	 */
	@Test
	public void getSlotsDataSuccessTest() throws Exception {
		ReflectionTestUtils.setField(parkingController, "isInitialized", true);

		when(parkingService.getAllParkingSlots())
				.thenReturn(Optional.of(PayNParkApplicationData.getParkingSlotDataList()));
		ResponseEntity<List<ParkingSlotDto>> responseEntity = parkingController.getParkingSlotDetail();

		assertFalse(responseEntity.getBody().isEmpty());
		assertEquals(responseEntity.getBody().size(), 1);
	}

	@Test
	public void getSlotsDataEmptyTest() throws Exception {
		ReflectionTestUtils.setField(parkingController, "isInitialized", false);
		when(parkingService.getAllParkingSlots()).thenReturn(Optional.empty());
		assertThrows(SlotsNotInitializedException.class, () -> parkingController.getParkingSlotDetail());
	}
}
