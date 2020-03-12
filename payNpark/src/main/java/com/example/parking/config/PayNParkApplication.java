package com.example.parking.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.parking.controller", "com.example.parking.config",
		"com.example.parking.service" })
@EntityScan("com.example.parking.model")
@EnableJpaRepositories("com.example.parking.repository")
public class PayNParkApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayNParkApplication.class, args);
	}

}
