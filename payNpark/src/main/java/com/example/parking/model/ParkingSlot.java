package com.example.parking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ParkingSlots")
public class ParkingSlot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "type", nullable = false)
	private String parkingSlotType;

	@Column(name = "free", nullable = true)
	private Boolean free;

	public ParkingSlot() {
		super();
	}

	public ParkingSlot(String parkingSlotType, Boolean free) {
		super();
		this.parkingSlotType = parkingSlotType;
		this.free = free;
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

	public Boolean isFree() {
		return free;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}
}
