package com.example.parking.dto;

public class VehicleDetails {

	private String vehicleType;

	private String vehicleNo;
	
	public VehicleDetails() {

	}

	public VehicleDetails(String type, String number) {
		this.vehicleType = type;
		this.vehicleNo = number;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
}
