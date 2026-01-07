package com.labmanager.labs.controller;

import com.labmanager.labs.entity.Equipment;
import com.labmanager.labs.service.LabService;
import com.labmanager.labs.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class LabControllerTest {

    @Test
    void getEquipmentReturnsList() {
        LabService labService = org.mockito.Mockito.mock(LabService.class);
        ReservationService resService = org.mockito.Mockito.mock(ReservationService.class);
        LabController controller = new LabController(labService, resService);

        Equipment e = new Equipment("lab1", "Oscilloscope", 5, 0);
        java.util.List<Equipment> list = java.util.List.of(e);
        org.mockito.Mockito.when(labService.getEquipment("lab1")).thenReturn(list);

        ResponseEntity<java.util.List<Equipment>> resp = controller.getEquipment("lab1");

        assertNotNull(resp);
        assertEquals(200, resp.getStatusCodeValue());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertEquals("Oscilloscope", resp.getBody().get(0).getName());
    }

}
