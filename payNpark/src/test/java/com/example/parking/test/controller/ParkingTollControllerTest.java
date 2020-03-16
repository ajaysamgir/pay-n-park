package com.example.parking.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.parking.PayNParkApplicationData;
import com.example.parking.controller.ParkingController;
import com.example.parking.model.ParkingSlot;
import com.example.parking.service.ParkingService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParkingController.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ParkingTollControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ParkingService service;

	//@Test
	public void getParkingSlotSuccessTest() throws Exception {
		ParkingSlot parkingSlot = PayNParkApplicationData.getParkingSlotData();

		// doReturn(Optional.of(parkingSlot)).when(service).getAvailableParkingSlot("MH12AD9415");

		mockMvc.perform(post("/api/initialize").contentType(MediaType.ALL)
				.content(toJson(PayNParkApplicationData.getParkingInitializer()))
				.accept(MediaType.ALL)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStandardParkingSlot", is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar20KWParkingSlot", is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar50KWParkingSlot", is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.fixedAmount", is(5.0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.hourPrice", is(1.2)));
	}

	private byte[] toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
