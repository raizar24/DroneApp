package com.drone.Service;

import com.drone.Model.Medication;
import com.drone.Repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    private static final int MEDICATION_ID = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMedication_Success() {
        // Arrange
        Medication medication = new Medication( "Biogesic", 200, "ABC123", "image.png");
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        // Act
        Medication result = medicationService.addMedication(medication);

        // Assert
        assertNotNull(result);
        assertEquals("Biogesic", result.getName());
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    void testUpdateMedication_Success() {
        // Arrange
        Medication existingMedication = new Medication( "Biogesic", 200, "ABC123", "image.png");
        Medication updatedMedication = new Medication( "Neozep", 250, "XYZ789", "newImage.png");

        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(existingMedication));
        when(medicationRepository.save(any(Medication.class))).thenReturn(updatedMedication);

        // Act
        Medication result = medicationService.updateMedication(MEDICATION_ID, updatedMedication);

        // Assert
        assertNotNull(result);
        assertEquals("Neozep", result.getName());
        assertEquals(250, result.getWeight());
        assertEquals("XYZ789", result.getCode());
        assertEquals("newImage.png", result.getImage());
        verify(medicationRepository, times(1)).save(existingMedication);
    }

    @Test
    void testUpdateMedication_NotFound() {
        // Arrange
        Medication updatedMedication = new Medication("Biogesic", 250, "XYZ789", "newImage.png");

        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicationService.updateMedication(MEDICATION_ID, updatedMedication);
        });

        assertEquals("Medication not found", exception.getMessage());
    }

    @Test
    void testGetAllMedications_Success() {
        // Arrange
        List<Medication> medications = Arrays.asList(
                new Medication( "Biogesic", 200, "ABC123", "image1.png"),
                new Medication( "Neozep", 250, "XYZ789", "image2.png")
        );
        when(medicationRepository.findAll()).thenReturn(medications);

        // Act
        List<Medication> result = medicationService.getAllMedications();

        // Assert
        assertEquals(2, result.size());
        verify(medicationRepository, times(1)).findAll();
    }

    @Test
    void testGetMedicationById_Success() {
        // Arrange
        Medication medication = new Medication( "Biogesic", 200, "ABC123", "image.png");
        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(medication));

        // Act
        Medication result = medicationService.getMedicationById(MEDICATION_ID);

        // Assert
        assertNotNull(result);
        assertEquals("Biogesic", result.getName());
        verify(medicationRepository, times(1)).findById(MEDICATION_ID);
    }

    @Test
    void testGetMedicationById_NotFound() {
        // Arrange
        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicationService.getMedicationById(MEDICATION_ID);
        });

        assertEquals("Medication not found", exception.getMessage());
    }

    @Test
    void testDeleteMedication_Success() {
        // Arrange
        when(medicationRepository.existsById(MEDICATION_ID)).thenReturn(true);
        doNothing().when(medicationRepository).deleteById(MEDICATION_ID);

        // Act
        medicationService.deleteMedication(MEDICATION_ID);

        // Assert
        verify(medicationRepository, times(1)).deleteById(MEDICATION_ID);
    }

    @Test
    void testDeleteMedication_NotFound() {
        // Arrange
        when(medicationRepository.existsById(MEDICATION_ID)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicationService.deleteMedication(MEDICATION_ID);
        });

        assertEquals("Medication not found", exception.getMessage());
    }
}
