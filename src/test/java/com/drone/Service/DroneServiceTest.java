package com.drone.Service;

import com.drone.Model.*;
import com.drone.Repository.DeliveryListRepository;
import com.drone.Repository.DroneRepository;
import com.drone.Repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DeliveryListRepository deliveryListRepository;

    @InjectMocks
    private DroneService droneService;

    private static final String DRONE_ID = "DR123";
    private static final int MEDICATION_ID = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadDrone_Success() {
        Drone drone = new Drone(DRONE_ID, DroneModel.LIGHTWEIGHT, 500, 50, DroneState.IDLE);
        Medication medication = new Medication( "Silimarin", 200, "ABC123", "image1.png");
        List<DeliveryList> existingDeliveries = new ArrayList<>();

        when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(medication));
        when(deliveryListRepository.findByDroneSerialNumber(DRONE_ID)).thenReturn(existingDeliveries);

        droneService.loadDrone(DRONE_ID, MEDICATION_ID, 2);

        verify(deliveryListRepository, times(1)).saveAll(anyList());
        verify(droneRepository, times(1)).save(any(Drone.class));
        assertEquals(DroneState.LOADING, drone.getDroneState());
    }

    @Test
    void testLoadDrone_ExceedsWeightLimit() {
        Drone drone = new Drone(DRONE_ID, DroneModel.LIGHTWEIGHT, 500, 50, DroneState.IDLE);
        Medication medication = new Medication( "BIOGESIC", 600, "XYZ123", "image.png");

        when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(medication));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            droneService.loadDrone(DRONE_ID, MEDICATION_ID, 1);
        });

        assertEquals("Total weight exceeds the drone's capacity of 500 grams.", exception.getMessage());
        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    void testLoadDrone_BatteryTooLow() {
        Drone drone = new Drone(DRONE_ID, DroneModel.LIGHTWEIGHT, 500, 20, DroneState.IDLE);
        Medication medication = new Medication( "NEOZEP", 100, "ABC123", "image1.png");

        when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(medication));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            droneService.loadDrone(DRONE_ID, MEDICATION_ID, 2);
        });

        assertEquals("Battery too low for loading!", exception.getMessage());
    }

    @Test
    void testLoadDrone_DroneNotFound() {
        when(droneRepository.findById("DR999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            droneService.loadDrone("DR999", MEDICATION_ID, 2);
        });

        assertEquals("Drone not found with serial number: DR999", exception.getMessage());
    }

    @Test
    void testLoadDrone_MedicationNotFound() {
        Drone drone = new Drone(DRONE_ID, DroneModel.LIGHTWEIGHT, 500, 50, DroneState.IDLE);
        when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            droneService.loadDrone(DRONE_ID, 999, 2);
        });

        assertEquals("Medication not found with ID: 999", exception.getMessage());
    }

    @Test
    void testAddMedicationAndExceedWeightLimit() {
        Drone drone = new Drone(DRONE_ID, DroneModel.LIGHTWEIGHT, 500, 50, DroneState.IDLE);
        Medication medication = new Medication("Paracetamol", 200, "PARA123", "image.png");
        medication.setId(1);
        List<DeliveryList> existingDeliveries = new ArrayList<>();
        when(droneRepository.findById(DRONE_ID)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(medication));
        when(deliveryListRepository.findByDroneSerialNumber(DRONE_ID)).thenReturn(existingDeliveries);

        // Load 2 quantity 400g
        droneService.loadDrone(DRONE_ID, MEDICATION_ID, 2);

        // check state loading
        assertEquals(DroneState.LOADING, drone.getDroneState());

        // check loaded 400g
        existingDeliveries.add(new DeliveryList(drone, medication, 2));
        when(deliveryListRepository.findByDroneSerialNumber(DRONE_ID)).thenReturn(existingDeliveries);

        // add another 200g, error exceed weight
        Exception exception = assertThrows(RuntimeException.class, () -> {
            droneService.loadDrone(DRONE_ID, MEDICATION_ID, 1);
        });

        assertEquals("Total weight exceeds the drone's capacity of 500 grams.", exception.getMessage());
    }
}
