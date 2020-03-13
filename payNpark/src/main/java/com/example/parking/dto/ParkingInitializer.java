package com.example.parking.dto;

import javax.validation.constraints.Size;

public class ParkingInitializer {

	private Integer totalCapacity;

	private Integer eCar20KWSlotSize;

	private Integer eCar50KWSlotSize;

	private Integer stdCarSlotSize;

	public ParkingInitializer() {
	}

	public ParkingInitializer(int e20kw, int e50wk, int std, int capacity) {
		this.eCar20KWSlotSize = e20kw;
		this.eCar50KWSlotSize = e50wk;
		this.stdCarSlotSize = std;
		this.totalCapacity = capacity;
	}

	public Integer getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(Integer totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public Integer geteCar20KWSlotSize() {
		return eCar20KWSlotSize;
	}

	public void seteCar20KWSlotSize(Integer eCar20KWSlotSize) {
		this.eCar20KWSlotSize = eCar20KWSlotSize;
	}

	public Integer geteCar50KWSlotSize() {
		return eCar50KWSlotSize;
	}

	public void seteCar50KWSlotSize(Integer eCar50KWSlotSize) {
		this.eCar50KWSlotSize = eCar50KWSlotSize;
	}

	public Integer getStdCarSlotSize() {
		return stdCarSlotSize;
	}

	public void setStdCarSlotSize(Integer stdCarSlotSize) {
		this.stdCarSlotSize = stdCarSlotSize;
	}

}
