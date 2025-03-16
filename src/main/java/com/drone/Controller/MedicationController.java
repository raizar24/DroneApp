package com.drone.Controller;


import com.drone.Model.Medication;
import com.drone.Service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/medications")
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> addMedication(@Valid @RequestBody Medication medication) {
        try {
            Medication savedMedication = medicationService.addMedication(medication);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Medication added or updated successfully");
            response.put("medication", savedMedication);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<?> getMedications(@RequestParam(required = false) Integer id) {
        if (id == null) {
            List<Medication> medications = medicationService.getAllMedications();
            return ResponseEntity.ok(medications);
        } else {
            try {
                Medication medication = medicationService.getMedicationById(id);
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("medication", medication);
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
    }




}
