package com.example.parking.dto;

import com.example.parking.model.ParkingSlot;

public class ParkingSlotDto {

	private Long id;

	private String parkingSlotType;

	private Boolean free;

	private String policy;

	private String parkedCar;

	private double rentPerHour;

	private double fixedAmount;

	public ParkingSlotDto() {
		super();
	}

	public ParkingSlotDto(Long id, String parkingSlotType, Boolean free, String policy, String parkedCar,
			double rentPerHour, double fixedRate) {
		super();
		this.id = id;
		this.parkingSlotType = parkingSlotType;
		this.free = free;
		this.policy = policy;
		this.parkedCar = parkedCar == null ? "none" : parkedCar;
		this.rentPerHour = rentPerHour;
		this.fixedAmount = fixedRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParkingSlotType() {
		return parkingSlotType;
	}

	public void setParkingSlotType(String parkingSlotType) {
		this.parkingSlotType = parkingSlotType;
	}

	public Boolean getFree() {
		return free;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getParkedCar() {
		return parkedCar;
	}

	public void setParkedCar(String parkedCar) {
		if (parkedCar == null || parkedCar.isEmpty()) {
			this.parkedCar = "none";
		} else {
			this.parkedCar = parkedCar;
		}

	}

	public double getRentPerHour() {
		return rentPerHour;
	}

	public void setRentPerHour(double rentPerHour) {
		this.rentPerHour = rentPerHour;
	}

	public double getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(double fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public static ParkingSlotDto fromDomain(ParkingSlot parkingSlot) {
		if (parkingSlot != null) {
			return new ParkingSlotDto(parkingSlot.getId(), parkingSlot.getParkingSlotType(), parkingSlot.isFree(),
					parkingSlot.getPolicy(), parkingSlot.getParkedCar(), parkingSlot.getRentPerHour(),
					parkingSlot.getFixedAmount());
		}
		return null;
	}
}
