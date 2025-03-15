package com.drone.Service;

import com.drone.Model.Medication;
import com.drone.Repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    // Create
    public Medication addMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    // Update by id
    public Medication updateMedication(int id, Medication updatedMedication) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        medication.setName(updatedMedication.getName());
        medication.setWeight(updatedMedication.getWeight());
        medication.setCode(updatedMedication.getCode());
        medication.setImage(updatedMedication.getImage());
        return medicationRepository.save(medication);
    }

    // Read all
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    // Read by ID
    public Medication getMedicationById(int id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
    }

    // Delete
    public void deleteMedication(int id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Medication not found");
        }
        medicationRepository.deleteById(id);
    }
}
