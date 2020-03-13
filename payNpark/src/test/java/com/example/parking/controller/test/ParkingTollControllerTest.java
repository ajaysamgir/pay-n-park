//package com.example.parking.controller.test;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.mockito.Mockito.doReturn;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import com.example.parking.controller.ParkingTollController;
//import com.example.parking.dto.ParkingSlotDto;
//import com.example.parking.dto.ParkingSlotType;
//import com.example.parking.dto.ParkingInitializer;
//import com.example.parking.model.PricingPolicy;
//import com.example.parking.service.ParkingTollService;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ParkingTollController.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
//class ParkingTollControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private ParkingTollService service;
//
//	//@Test
//	public void getParkingSlotSuccessTest() throws Exception {
//		ParkingSlotDto parkingSlotDetails = new ParkingSlotDto(1L, ParkingSlotType.STANDARD, true);
//
//		doReturn(Optional.of(parkingSlotDetails)).when(service).getAvailableParkingSlot("MH12AD9415");
//
//		// mockMvc.perform(get("/getparking/MH12AD9415").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//		PricingPolicy pricingPolicy = new PricingPolicy(5, 1.2);
//		// PricingPolicy newPricingPolicy = new PricingPolicy(7, 1.0);
//		//ParkingInitializer tollParkingConfig = new ParkingInitializer(10, 10, 10, pricingPolicy.getFixedAmount(), pricingPolicy.getHourPrice());
//		mockMvc.perform(post("/init").content(toJson(tollParkingConfig)).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStandardParkingSlot", is(10)))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar20KWParkingSlot", is(10)))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar50KWParkingSlot", is(10)))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.fixedAmount", is(5.0)))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.hourPrice", is(1.2)));
//	}
//
//	private byte[] toJson(Object object) throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setSerializationInclusion(Include.NON_NULL);
//		return mapper.writeValueAsBytes(object);
//	}
//}
