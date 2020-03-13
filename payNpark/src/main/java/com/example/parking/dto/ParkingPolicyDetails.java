package com.example.parking.dto;

public class ParkingPolicyDetails {

	private String policyType;
	private boolean isFree;

	public ParkingPolicyDetails() {

	}

	public ParkingPolicyDetails(String policyType) {
		
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

}
