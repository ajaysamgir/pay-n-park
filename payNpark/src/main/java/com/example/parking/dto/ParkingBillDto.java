package com.example.parking.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;

public class ParkingBillDto {

	private Long id;

	private String vehicleNo;

	private ParkingSlotDto parkingSlot;

	@Column(name = "price", nullable = false)
	private Double price;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;

	public ParkingBillDto(String vehicleNo, ParkingSlotDto parkingSlot, LocalDateTime time) {
		this.vehicleNo = vehicleNo;
		this.parkingSlot = parkingSlot;
		this.startTime = time;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public ParkingSlotDto getParkingSlot() {
		return parkingSlot;
	}

	public void setParkingSlot(ParkingSlotDto parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

}
