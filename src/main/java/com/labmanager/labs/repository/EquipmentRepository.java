package com.labmanager.labs.repository;

import java.util.List;
import java.util.Optional;

import com.labmanager.labs.entity.Equipment;
import com.labmanager.labs.entity.EquipmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, EquipmentId> {
    List<Equipment> findAllByIdLabId(String labId);
    Optional<Equipment> findByIdLabIdAndIdName(String labId, String Name);  // returns only one
}

