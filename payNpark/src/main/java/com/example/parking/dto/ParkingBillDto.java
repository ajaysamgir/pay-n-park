package com.example.parking.dto;

import java.time.LocalDateTime;

import com.example.parking.model.ParkingBill;

public class ParkingBillDto {

	private Long id;

	private String vehicleNo;

	private ParkingSlotDto parkingSlot;

	private Double billAmount;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	public ParkingBillDto(String vehicleNo, ParkingSlotDto parkingSlot, Double totalPrice, LocalDateTime startTime,
			LocalDateTime endTime) {
		this.vehicleNo = vehicleNo;
		this.parkingSlot = parkingSlot;
		this.startTime = startTime;
		this.endTime = endTime;
		this.billAmount = totalPrice;
	}

	public ParkingBillDto() {
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

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public static ParkingBillDto fromDomain(ParkingBill bill) {
		ParkingSlotDto parkingSlotDto = ParkingSlotDto.fromDomain(bill.getParkingSlot());
		return new ParkingBillDto(bill.getCarNumber(), parkingSlotDto, bill.getTotalBill(), bill.getStartTime(),
				bill.getEndTime());
	}

}
