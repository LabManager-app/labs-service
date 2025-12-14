package com.labmanager.labs.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labmanager.labs.entity.Equipment;
import com.labmanager.labs.entity.Lab;
import com.labmanager.labs.service.LabService;
import com.labmanager.labs.dto.EquipmentRequest;

@RestController
@RequestMapping("/labs")
public class LabController {
    
    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping({"", "/"})        
    public ResponseEntity<List<Lab>> getLabs() {
        List<Lab> labs = labService.getLabs();
        return ResponseEntity.ok(labs);
    }

    @PostMapping
    public ResponseEntity<Boolean> addLab(@RequestBody Map<String, String> body) {
        boolean added = labService.addLab(body.get("labId"), body.get("location"));
        return ResponseEntity.ok(added);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> removeLab(@RequestParam("labId") String labId) {
        boolean removed = labService.removeLab(labId);
        return ResponseEntity.ok(removed);
    }


    @PostMapping("/{labId}/equipment")
    public ResponseEntity<Boolean> addEquipmentItem(@PathVariable("labId") String labId,
            @RequestBody List<EquipmentRequest> equipment) {
        boolean ok = labService.addEquipment(labId, equipment);
        return ResponseEntity.ok(ok);
    }

    @GetMapping("/{labId}/equipment")
    public ResponseEntity<List<Equipment>> getEquipment(@PathVariable("labId") String labId){
        List<Equipment> ok = labService.getEquipment(labId);
        return ResponseEntity.ok(ok);
    }


}


