package com.example.parking.config;

public class TollParkingInitializer {

	private Integer electricCar20KWParkingSlotSize;

	private Integer electricCar50KWParkingSlotSize;

	private Integer electricCarStandardParkingSlotSize;

	private double fixedAmount;

	private double hourPrice;

	public TollParkingInitializer() {
	}

	public TollParkingInitializer(int i, int j, int k, double fixedAmmout, double hourPrice) {
		this.electricCar20KWParkingSlotSize = i;
		this.electricCar50KWParkingSlotSize = j;
		this.electricCarStandardParkingSlotSize = k;
		this.fixedAmount = fixedAmmout;
		this.hourPrice = hourPrice;
	}

	public Integer getElectricCar20KWParkingSlotSize() {
		return electricCar20KWParkingSlotSize;
	}

	public void setElectricCar20KWParkingSlotSize(Integer electricCar20KWParkingSlotSize) {
		this.electricCar20KWParkingSlotSize = electricCar20KWParkingSlotSize;
	}

	public Integer getElectricCar50KWParkingSlotSize() {
		return electricCar50KWParkingSlotSize;
	}

	public void setElectricCar50KWParkingSlotSize(Integer electricCar50KWParkingSlotSize) {
		this.electricCar50KWParkingSlotSize = electricCar50KWParkingSlotSize;
	}

	public Integer getElectricCarStandardParkingSlotSize() {
		return electricCarStandardParkingSlotSize;
	}

	public void setElectricCarStandardParkingSlotSize(Integer electricCarStandardParkingSlotSize) {
		this.electricCarStandardParkingSlotSize = electricCarStandardParkingSlotSize;
	}

	public double getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(double fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public double getHourPrice() {
		return hourPrice;
	}

	public void setHourPrice(double hourPrice) {
		this.hourPrice = hourPrice;
	}

}
