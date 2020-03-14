package com.example.parking.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CarDetails {

	@NotNull
	@NotEmpty
	String carNumber;

	@NotNull
	@NotEmpty
	String carType;

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

}
