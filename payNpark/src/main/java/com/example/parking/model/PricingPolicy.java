package com.example.parking.model;

public class PricingPolicy {

	private double fixedAmount;

	private double hourPrice;

	public PricingPolicy(double fixedAmount, double hourPrice) {
		this.fixedAmount = fixedAmount;
		this.hourPrice = hourPrice;
	}

	public double getFixedAmount() {
		return fixedAmount;
	}

	public double getHourPrice() {
		return hourPrice;
	}

	public void setFixedAmount(double fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public void setHourPrice(double hourPrice) {
		this.hourPrice = hourPrice;
	}
}