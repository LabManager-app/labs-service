package com.labmanager.labs.repository;

import java.util.List;
import java.util.Optional;

import com.labmanager.labs.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findAllByLabId(String labId);
    Optional<Equipment> findByLabIdAndName(String labId, String name);  // returns only one
}

