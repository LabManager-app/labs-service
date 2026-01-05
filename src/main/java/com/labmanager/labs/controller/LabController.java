package com.labmanager.labs.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labmanager.labs.entity.Equipment;
import com.labmanager.labs.entity.Lab;
import com.labmanager.labs.service.LabService;
import com.labmanager.labs.service.ReservationService;
import com.labmanager.labs.dto.EquipmentRequest; 

@RestController
@RequestMapping("/labs")
public class LabController {
    
    private final LabService labService;
    private final ReservationService resService;

    public LabController(LabService labService, ReservationService resService) {
        this.labService = labService;
        this.resService = resService;
    }

    // labs
    @GetMapping({"", "/"})        
    public ResponseEntity<List<Lab>> getLabs() {
        List<Lab> labs = labService.getLabs();
        return ResponseEntity.ok(labs);
    }

    @PostMapping
    public ResponseEntity<Lab> addLab(@RequestBody Map<String, String> body) {
        Lab lab = labService.addLab(body.get("labId"), body.get("location"));
        if (lab == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(lab);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> removeLab(@RequestParam("labId") String labId) {
        boolean removed = labService.removeLab(labId);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // ocupation
    @PatchMapping("/{labId}/occupation")
    public ResponseEntity<Boolean> setOccupation(
        @PathVariable String labId,
        @RequestBody(required = false) Map<String,String> body) {

        if (body != null) {
            String occ = body.get("occupactionType");
            boolean ok = labService.occupyLab(labId, occ); // set + save
            return ok ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
        } else {
            boolean ok = labService.freeLab(labId); // sets "Available" + save
            return ok ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
        }
    }


    // equipment
    @GetMapping("/{labId}/equipment")
    public ResponseEntity<List<Equipment>> getEquipment(@PathVariable("labId") String labId){
        List<Equipment> ok = labService.getEquipment(labId);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/{labId}/equipment")
    public ResponseEntity<List<Equipment>> addEquipmentItem(@PathVariable("labId") String labId,
                                                            @RequestBody List<EquipmentRequest> equipment) {
        boolean ok = labService.addEquipment(labId, equipment);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        // return the current equipment list for the lab as the created resource representation
        List<Equipment> updated = labService.getEquipment(labId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @DeleteMapping("/{labId}/equipment/{id}")
    public ResponseEntity<Void> removeEquipment(@PathVariable("id") Long id, @RequestParam("quantity") int quantity) {
        boolean removed = labService.removeEquipment(id, quantity);
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/equipment")
    public ResponseEntity<List<String>> getEquipmentList(){
        List<String> eq = labService.getEquipmentList();
        return ResponseEntity.ok(eq);
    }


    // reservation
    // return labs that have requested equipment available
    @PostMapping("/reservation")
    public ResponseEntity<List<Lab>> getAvailableLabs(@RequestBody(required = false) List<EquipmentRequest> equipmentRequest){
        List<Lab> eq = resService.checkAvailability(equipmentRequest);
        return ResponseEntity.ok(eq);
    }

    // reserves euipment in equipment request
    @PostMapping("/{labId}/reservation")
    public ResponseEntity<Boolean> reserveEquipment(@PathVariable("labId") String labId,
                                                    @RequestBody List<EquipmentRequest> equipmentRequest){
        boolean ok = resService.reserve(labId, equipmentRequest);
        if (ok) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        // reservation failed (e.g., insufficient equipment or lab not found)
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // free previously reserved equipment for a lab
    @PostMapping("/{labId}/reservation/free")
    public ResponseEntity<Boolean> freeReservation(@PathVariable("labId") String labId,
                                                   @RequestBody List<EquipmentRequest> equipmentRequest){
        boolean ok = resService.free(labId, equipmentRequest);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}


