package com.example.parking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.parking.repository.ParkingBillRepository;
import com.example.parking.repository.ParkingSlotRepository;
import com.example.parking.service.ParkingService;
import com.example.parking.service.ParkingServiceImpl;

@Configuration
@EnableWebMvc
@EnableJpaRepositories
public class AppConfig {

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;
	
	@Autowired ParkingBillRepository parkingBillRepository;
	
	@Bean
	public ParkingService getParkingTollService() {
		return new ParkingServiceImpl(parkingSlotRepository, parkingBillRepository);
	}
	
}
