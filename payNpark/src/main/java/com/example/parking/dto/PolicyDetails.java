package com.example.parking.dto;

public class PolicyDetails {
	
	private String policyType;
	
	private Long parkingSlotNo;

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
}
