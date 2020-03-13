package com.example.parking.dto;

public class ParkingInitializer {

	private Integer totalCapacity;

	private Integer electric20KWCar;

	private Integer electric50KWCar;

	private Integer standardCar;

	public ParkingInitializer() {
	}

	public ParkingInitializer(int e20kw, int e50wk, int std, int capacity) {
		this.electric20KWCar = e20kw;
		this.electric50KWCar = e50wk;
		this.standardCar = std;
		this.totalCapacity = capacity;
	}

	public Integer getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(Integer totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public Integer getElectric20KWCar() {
		return electric20KWCar;
	}

	public void setElectric20KWCar(Integer electric20kwCar) {
		electric20KWCar = electric20kwCar;
	}

	public Integer getElectric50KWCar() {
		return electric50KWCar;
	}

	public void setElectric50KWCar(Integer electric50kwCar) {
		electric50KWCar = electric50kwCar;
	}

	public Integer getStandardCar() {
		return standardCar;
	}

	public void setStandardCar(Integer standardCar) {
		this.standardCar = standardCar;
	}
}
