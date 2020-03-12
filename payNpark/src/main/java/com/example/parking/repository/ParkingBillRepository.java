package com.example.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.parking.model.ParkingBill;

public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long>{

}
