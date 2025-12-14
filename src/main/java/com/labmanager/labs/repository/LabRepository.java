package com.labmanager.labs.repository;

import com.labmanager.labs.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabRepository extends JpaRepository<Lab, String> {
}