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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

// Swagger API dokumentacija: http://localhost:8080/swagger-ui/index.html#/

@RestController
@RequestMapping("/labs")
@Tag(name = "Labs", description = "API za upravljanje laboratorijev in opreme")
public class LabController {
    
    private final LabService labService;
    private final ReservationService resService;

    public LabController(LabService labService, ReservationService resService) {
        this.labService = labService;
        this.resService = resService;
    }

    // labs
    @Operation(summary = "Pridobi vse laboratorije", description = "Vrne seznam vseh laboratorijev v sistemu")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Uspešno pridobljen seznam laboratorijev")
    })
    @GetMapping({"", "/"})        
    public ResponseEntity<List<Lab>> getLabs() {
        List<Lab> labs = labService.getLabs();
        return ResponseEntity.ok(labs);
    }

    @Operation(summary = "Ustvari laboratorij", description = "Ustvari nov laboratorij z unikatnim `labId` in lokacijo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Laboratorij uspešno ustvarjen"),
        @ApiResponse(responseCode = "409", description = "Laboratorij z istim labId že obstaja")
    })
    @PostMapping
    public ResponseEntity<Lab> addLab(@RequestBody Map<String, String> body) {
        Lab lab = labService.addLab(body.get("labId"), body.get("location"));
        if (lab == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(lab);
    }

    @Operation(summary = "Odstrani laboratorij", description = "Odstrani laboratorij po `labId`. Vrne 204, če uspešno, ali 404, če ni najden.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Laboratorij odstranjen"),
        @ApiResponse(responseCode = "404", description = "Laboratorij ni najden")
    })
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
    @Operation(summary = "Nastavi zasedenost laboratorija", description = "Nastavi ali počisti zasedenost laboratorija. Če je telo prazno, se laboratorij sprosti.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Zasedenost posodobljena"),
        @ApiResponse(responseCode = "404", description = "Laboratorij ni najden")
    })
    @PatchMapping("/{labId}/occupation")
    public ResponseEntity<Boolean> setOccupation(
        @Parameter(description = "ID laboratorija") @PathVariable String labId,
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
    @Operation(summary = "Pridobi opremo laboratorija", description = "Vrne seznam opreme za podani `labId`.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seznam opreme vrnjen"),
        @ApiResponse(responseCode = "404", description = "Laboratorij ni najden")
    })
    @GetMapping("/{labId}/equipment")
    public ResponseEntity<List<Equipment>> getEquipment(@Parameter(description = "ID laboratorija") @PathVariable("labId") String labId){
        List<Equipment> ok = labService.getEquipment(labId);
        return ResponseEntity.ok(ok);
    }

    @Operation(summary = "Dodaj opremo v laboratorij", description = "Doda eno ali več kosov opreme v laboratorij; vrne posodobljen seznam opreme.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Oprema dodana"),
        @ApiResponse(responseCode = "404", description = "Laboratorij ni najden")
    })
    @PostMapping("/{labId}/equipment")
    public ResponseEntity<List<Equipment>> addEquipmentItem(@Parameter(description = "ID laboratorija") @PathVariable("labId") String labId,
                                                            @RequestBody List<EquipmentRequest> equipment) {
        boolean ok = labService.addEquipment(labId, equipment);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        // return the current equipment list for the lab as the created resource representation
        List<Equipment> updated = labService.getEquipment(labId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @Operation(summary = "Odstrani opremo", description = "Odstrani določeno količino opreme (po `id`) iz sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Oprema odstranjena"),
        @ApiResponse(responseCode = "404", description = "Oprema ali laboratorij ni najden")
    })
    @DeleteMapping("/{labId}/equipment/{id}")
    public ResponseEntity<Void> removeEquipment(@Parameter(description = "ID opreme") @PathVariable("id") Long id, @RequestParam("quantity") int quantity) {
        boolean removed = labService.removeEquipment(id, quantity);
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Seznam vrst opreme", description = "Vrne seznam vseh različnih vrst opreme v sistemu.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seznam vrst opreme vrnjen")
    })
    @GetMapping("/equipment")
    public ResponseEntity<List<String>> getEquipmentList(){
        List<String> eq = labService.getEquipmentList();
        return ResponseEntity.ok(eq);
    }


    // reservation
    // return labs that have requested equipment available
    @Operation(summary = "Preveri razpoložljivost laboratorijev", description = "Vrne laboratorije, ki izpolnjujejo zahtevano opremo (če ni telesa, vrne vse razpoložljive).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seznam razpoložljivih laboratorijev vrnjen")
    })
    @PostMapping("/reservation")
    public ResponseEntity<List<Lab>> getAvailableLabs(@RequestBody(required = false) List<EquipmentRequest> equipmentRequest){
        List<Lab> eq = resService.checkAvailability(equipmentRequest);
        return ResponseEntity.ok(eq);
    }

    // reserves euipment in equipment request
    @Operation(summary = "Rezerviraj opremo v laboratoriju", description = "Poskusi rezervirati zahtevano opremo v izbranem laboratoriju.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Rezervacija uspešna"),
        @ApiResponse(responseCode = "409", description = "Rezervacija ni možna zaradi nezadostne opreme ali napake")
    })
    @PostMapping("/{labId}/reservation")
    public ResponseEntity<Boolean> reserveEquipment(@Parameter(description = "ID laboratorija") @PathVariable("labId") String labId,
                                                    @RequestBody List<EquipmentRequest> equipmentRequest){
        boolean ok = resService.reserve(labId, equipmentRequest);
        if (ok) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        // reservation failed (e.g., insufficient equipment or lab not found)
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // free previously reserved equipment for a lab
    @Operation(summary = "Razveljavi rezervacijo", description = "Razbremeni (free) prej rezervirano opremo v laboratoriju.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rezervacija razveljavljena"),
        @ApiResponse(responseCode = "404", description = "Laboratorij ali rezervacija ni najdena")
    })
    @PostMapping("/{labId}/reservation/free")
    public ResponseEntity<Boolean> freeReservation(@Parameter(description = "ID laboratorija") @PathVariable("labId") String labId,
                                                   @RequestBody List<EquipmentRequest> equipmentRequest){
        boolean ok = resService.free(labId, equipmentRequest);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}


