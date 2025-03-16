package com.drone.Service;

import com.drone.Model.*;
import com.drone.Repository.DeliveryListRepository;
import com.drone.Repository.DroneRepository;
import com.drone.Repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private DeliveryListRepository deliveryListRepository;

    private static final int BATTERY_THRESHOLD = 25;

    // Register a new drone
    public void registerDrone(String serialNumber, DroneModel droneModel, int weightLimit, int batteryCapacity, DroneState droneState) {
        if (droneRepository.existsById(serialNumber)) {
            throw new RuntimeException("Drone with serial number " + serialNumber + " already exists.");
        }
        Drone drone = new Drone(serialNumber, droneModel, weightLimit, batteryCapacity, droneState);
        droneRepository.save(drone);
    }

    // Get drone details
    public Drone getDroneInfo(String serialNumber) {
        return droneRepository.findById(serialNumber)
                .orElseThrow(() -> new RuntimeException("Drone not found with serial number: " + serialNumber));
    }

    // Load a drone with medication
    public void loadDrone(String serialNumber, int medicationId, int quantity) {
        Drone drone = droneRepository.findById(serialNumber)
                .orElseThrow(() -> new RuntimeException("Drone not found with serial number: " + serialNumber));

        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + medicationId));

        List<DeliveryList> deliveries = deliveryListRepository.findByDroneSerialNumber(serialNumber);

        int totalWeight = 0;
        boolean medicationExists = false;

        for (DeliveryList delivery : deliveries) {
            totalWeight += delivery.getTotalWeight();
            if (delivery.getMedication().getId() == medicationId) {
                delivery.setQuantity(delivery.getQuantity() + quantity);
                medicationExists = true;
            }
        }

        totalWeight += medication.getWeight() * quantity;

        if (totalWeight > drone.getWeightLimit()) {
            throw new RuntimeException("Total weight exceeds the drone's capacity of " + drone.getWeightLimit() + " grams.");
        }

        if (!medicationExists) {
            DeliveryList newDelivery = new DeliveryList(drone, medication, quantity);
            deliveries.add(newDelivery);
        }

        if (drone.getBatteryCapacity() < BATTERY_THRESHOLD ) {
            throw new RuntimeException("Battery too low for loading!");
        }

        deliveryListRepository.saveAll(deliveries);
        drone.setDroneState(totalWeight == drone.getWeightLimit() ? DroneState.LOADED : DroneState.LOADING);
        droneRepository.save(drone);
    }

    // Get all loaded medications for a specific drone
    public List<DeliveryList> getLoadedMedications(String serialNumber) {
        return deliveryListRepository.findByDroneSerialNumber(serialNumber);
    }

    // Update drone state
    public void updateDroneState(String serialNumber, DroneState newState) {
        Drone drone = droneRepository.findById(serialNumber)
                .orElseThrow(() -> new RuntimeException("Drone not found with serial number: " + serialNumber));
        drone.setDroneState(newState);
        droneRepository.save(drone);
    }

    // Get available drones for loading
    public List<Drone> getAvailableDrones() {
        return droneRepository.findByDroneStateIn(Arrays.asList(DroneState.IDLE, DroneState.LOADING));
    }

    // Get all drones
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    // Get battery level of a drone
    public int getDroneBatteryLevel(String serialNumber) {
        Drone drone = droneRepository.findById(serialNumber)
                .orElseThrow(() -> new RuntimeException("Drone not found with serial number: " + serialNumber));
        return drone.getBatteryCapacity();
    }
}
