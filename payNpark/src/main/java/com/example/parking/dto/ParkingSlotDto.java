package com.example.parking.dto;

import com.example.parking.model.ParkingSlot;

public class ParkingSlotDto {

	private Long id;

	private String parkingSlotType;

	private Boolean free;

	private String policy;

	private String parkedCar;

	public ParkingSlotDto() {
		super();
	}

	public ParkingSlotDto(Long id, String parkingSlotType, Boolean free, String policy, String parkedCar) {
		super();
		this.id = id;
		this.parkingSlotType = parkingSlotType;
		this.free = free;
		this.policy = policy;
		this.parkedCar = parkedCar;
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

	public static ParkingSlotDto fromDomain(ParkingSlot parkingSlot) {
		if (parkingSlot != null) {
			return new ParkingSlotDto(parkingSlot.getId(), parkingSlot.getParkingSlotType(), parkingSlot.isFree(),
					parkingSlot.getPolicy(), parkingSlot.getParkedCar());
		}
		return null;
	}

	public String getParkedCar() {
		return parkedCar;
	}

	public void setParkedCar(String parkedCar) {
		if (parkedCar == null || parkedCar.isEmpty()) {
			parkedCar = "none";
		}
		
		this.parkedCar = parkedCar;
	}

}
