package com.labmanager.labs.service;

import java.util.List;
import java.util.Optional;

import com.labmanager.labs.entity.Lab;
import com.labmanager.labs.dto.EquipmentRequest;
import com.labmanager.labs.entity.Equipment;
import com.labmanager.labs.repository.EquipmentRepository;
import com.labmanager.labs.repository.LabRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class LabService {
    private final LabRepository labRepo;
    private final EquipmentRepository equipmentRepo;

    public LabService(LabRepository labRepository, EquipmentRepository equipmentRepository) {
        this.labRepo = labRepository;
        this.equipmentRepo = equipmentRepository;
    }

    // --- Labs ---
    public List<Lab> getLabs(){
        return labRepo.findAll();
    }

    public Lab addLab(String labId, String location) {
        if (labRepo.existsById(labId)) return null;
        Lab lab = new Lab(labId, location);
        labRepo.save(lab);
        return lab;
    }

    // deletes the lab and all it's equipment from database
    public boolean removeLab(String labId) {
        if (!labRepo.existsById(labId)) return false;

        try {
            List<Equipment> labEquipment = equipmentRepo.findAllByLabId(labId);
            equipmentRepo.deleteAll(labEquipment);
            labRepo.deleteById(labId);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }

    // sets lab as occupied
    public boolean occupyLab(String labId, String occupactionType){
        Optional<Lab> maybeLab = labRepo.findById(labId);
        if (maybeLab.isEmpty()) return false;   // lab ne obstaja

        Lab lab = maybeLab.get();
        lab.setOccupactionType(occupactionType);
        return true;
    }

    // sets lab as available
    public boolean freeLab(String labId){
        Optional<Lab> maybeLab = labRepo.findById(labId);
        if (maybeLab.isEmpty()) return false;   // lab ne obstaja
        
        Lab lab = maybeLab.get();
        lab.setOccupactionType("Available");
        return true;
    }


    // --- equipment ---
    public List<Equipment> getEquipment(String labId){
        return equipmentRepo.findAllByLabId(labId);
    }

    // addEquipment: adds a new equipment item to the given lab
    // if equipment already exists, increase its stock by 'stock'.
    public boolean addEquipment(String labId, List<EquipmentRequest> items) {

        for (EquipmentRequest item : items){
            String name = item.getName();
            int stock = item.getStock();

            // Check if this equipment already exists in this lab
            Optional<Equipment> inLab = equipmentRepo.findByLabIdAndName(labId, name);

            if (inLab.isPresent()) {
                Equipment eq = inLab.get();
                eq.setStock(eq.getStock() + stock);
                equipmentRepo.save(eq);

            } else {
                Equipment newEq = new Equipment(labId, name, stock, 0);
                equipmentRepo.save(newEq);
            }
        }
        return true;
    }
    

    // removes quantity from equipment identified by its id
    public boolean removeEquipment(Long id, int quantity) {
        Optional<Equipment> maybe = equipmentRepo.findById(id);
        if (maybe.isEmpty()) return false;

        Equipment eq = maybe.get();
        int newStock = eq.getStock() - quantity;

        try {
            if (newStock <= 0) {
                equipmentRepo.deleteById(id);
            } else {
                eq.setStock(newStock);
                equipmentRepo.save(eq);
            }
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }


}
