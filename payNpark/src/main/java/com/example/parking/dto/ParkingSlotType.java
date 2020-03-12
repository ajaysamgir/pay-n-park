package com.example.parking.dto;

public enum ParkingSlotType {
	STANDARD(0) {
		public boolean isElectricCar() {
			return false;
		}
	},
	ELECTRIC_CAR_20KW(20) {
		public boolean isElectricCar() {
			return true;
		}
	},
	ELECTRIC_CAR_50KW(20) {
		public boolean isElectricCar() {
			return true;
		}
	};

	private int powerSupply;

	public boolean isElectricCar() {
		return false;
	}
	
	public int getPowerSupply() {
		return this.powerSupply;
	}

	ParkingSlotType(int powerSupply) {
		this.powerSupply = powerSupply;
	}
}
