package com.example.parking.service.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.parking.dto.ParkingSlotDto;
import com.example.parking.dto.ParkingSlotType;
import com.example.parking.model.ParkingSlot;
import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;
import com.example.parking.service.ParkingTollService;
import com.example.parking.service.ParkingTollServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParkingTollServiceImpl.class)
public class ParkingTollServiceTest {

	@MockBean
	private ParkingSlotRepository parkingSlotRepository;
	
	@MockBean
	private ParkingBillRepository parkingBillRepository;

	private ParkingTollService service; // = new ParkingTollServiceImpl();

	@Before(value = "")
	public void setup() {
		service = new ParkingTollServiceImpl(parkingSlotRepository, parkingBillRepository);
	}

	@Test
	public void getParkingSlotSuccessTest() {
		service = new ParkingTollServiceImpl(parkingSlotRepository, parkingBillRepository);
		
		ParkingSlot parkingSlot = new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, true);
		parkingSlot.setId(1L);
		when(parkingSlotRepository.findAll()).thenReturn(Arrays.asList(parkingSlot));

		String plateNumber = "MH12AD9415";
		Optional<ParkingSlotDto> parkingSlotDto = service.getAvailableParkingSlot(plateNumber);
		assertTrue(parkingSlotDto.isPresent());
		assertTrue(parkingSlotDto.get() instanceof ParkingSlotDto);
	}
}
