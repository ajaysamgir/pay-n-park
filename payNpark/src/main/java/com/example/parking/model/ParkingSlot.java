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

	@Column(name = "policy", nullable = false)
	private String policy;
	
	@Column(name = "parkedCar", nullable = true)
	private String parkedCar;

	public ParkingSlot() {
		super();
	}

	public ParkingSlot(String parkingSlotType, Boolean free, String policy) {
		super();
		this.parkingSlotType = parkingSlotType;
		this.free = free;
		this.policy = policy;
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
		this.parkedCar = parkedCar;
	}
	
	
}