package com.example.parking.dto;

import com.example.parking.model.ParkingSlot;

public class ParkingSlotDto {

	private Long id;

	private ParkingSlotType parkingSlotType;

	private Boolean free;

	public ParkingSlotDto() {
		super();
	}

	public ParkingSlotDto(Long id, ParkingSlotType parkingSlotType, Boolean free) {
		super();
		this.id = id;
		this.parkingSlotType = parkingSlotType;
		this.free = free;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParkingSlotType getParkingSlotType() {
		return parkingSlotType;
	}

	public void setParkingSlotType(ParkingSlotType parkingSlotType) {
		this.parkingSlotType = parkingSlotType;
	}

	public Boolean getFree() {
		return free;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}

	public static ParkingSlotDto fromDomain(ParkingSlot parkingSlot) {
		if (parkingSlot != null) {
			return new ParkingSlotDto(parkingSlot.getId(), parkingSlot.getParkingSlotType(), parkingSlot.isFree());
		}
		return null;
	}
}
