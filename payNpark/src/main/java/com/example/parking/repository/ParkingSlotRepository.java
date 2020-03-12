package com.example.parking.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.parking.model.ParkingSlot;

@Repository
@Transactional
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
}
