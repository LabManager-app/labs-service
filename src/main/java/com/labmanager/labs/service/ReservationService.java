package com.labmanager.labs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.labmanager.labs.dto.EquipmentRequest;
import com.labmanager.labs.entity.Equipment;
import com.labmanager.labs.entity.Lab;
import com.labmanager.labs.repository.EquipmentRepository;
import com.labmanager.labs.repository.LabRepository;

import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final LabRepository labRepo;
    private final EquipmentRepository equipmentRepo;

    public ReservationService(LabRepository labRepository, EquipmentRepository equipmentRepository) {
        this.labRepo = labRepository;
        this.equipmentRepo = equipmentRepository;
    }

    // return all Lab objects where every requested item exists and has enough available units
    public List<Lab> checkAvailability(List<EquipmentRequest> equipmentRequest) {
        List<Lab> okLabs = new ArrayList<>();

        // all labs with occupactionType == "Available" (null-safe, case-insensitive)
        List<Lab> allLabs = labRepo.findAll()
            .stream()
            .filter(l -> {
                String t = l.getOccupactionType();
                return t != null && "available".equalsIgnoreCase(t);
            })
            .collect(Collectors.toList());

        if (equipmentRequest == null || equipmentRequest.isEmpty()) {
            return allLabs;
        }

        for (Lab lab : allLabs) {
            boolean labOk = true;
            // check for every equipment if it is ok
            for (EquipmentRequest req : equipmentRequest) {
                Optional<Equipment> eqFound = equipmentRepo.findByLabIdAndName(lab.getLabId(), req.getName());

                if (eqFound.isEmpty()) { labOk = false; break; } // lab does not have one

                // lab has it, check stock
                Equipment equipment = eqFound.get();
                int available = equipment.getStock() - equipment.getCurrentUsage();
                if (available < req.getStock()) { labOk = false; break; }
            }
            if (labOk) okLabs.add(lab);
        }
        return okLabs;
    }

    // for a specified lab, it reserves listed equipment + additional check
    public Boolean reserve(String labId, List<EquipmentRequest> equipmentRequest){
        if (labId == null || equipmentRequest == null) return false;

        for (EquipmentRequest req : equipmentRequest) {
            Optional<Equipment> eqFound = equipmentRepo.findByLabIdAndName(labId, req.getName());
            if (eqFound.isEmpty()) {
                return false; // equipment missing in lab
            }
            Equipment equipment = eqFound.get();
            int newUsage = equipment.getCurrentUsage() + req.getStock();
            if (newUsage > equipment.getStock()) {
                return false; // not enough available units
            }
            equipment.setCurrentUsage(newUsage);
            equipmentRepo.save(equipment);
        }
        return true;
    }

    // TO FIX
    public boolean free(String labId, List<EquipmentRequest> equipmentRequest){
        for (EquipmentRequest req : equipmentRequest) {
            Optional<Equipment> eqFound = equipmentRepo.findByLabIdAndName(labId, req.getName());
            Equipment equipment = eqFound.get();
            equipment.setCurrentUsage(equipment.getCurrentUsage() - req.getStock());
            equipmentRepo.save(equipment);
        }
        return true;
    }

}
