package com.example.parking.dto;

public class PolicyDetails {
	
	private String policyType;
	
	private Long parkingSlotNo;
	
	private double rate;

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public Long getParkingSlotNo() {
		return parkingSlotNo;
	}

	public void setParkingSlotNo(Long parkingSlotNo) {
		this.parkingSlotNo = parkingSlotNo;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}
