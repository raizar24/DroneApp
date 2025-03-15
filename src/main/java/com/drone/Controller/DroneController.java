package com.drone.Controller;

import com.drone.Model.DeliveryList;
import com.drone.Model.Drone;
import com.drone.Model.DroneState;
import com.drone.Service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    // Get all drones
    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        return ResponseEntity.ok(droneService.getAllDrones());
    }

    // Registering a drone
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Drone drone) {
        droneService.registerDrone(
                drone.getSerialNumber(),
                drone.getDroneModel(),
                drone.getWeightLimit(),
                drone.getBatteryCapacity(),
                drone.getDroneState()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Drone registered successfully");
        response.put("serialNumber", drone.getSerialNumber());

        return ResponseEntity.ok(response);
    }

    // Loading a drone with medication
    @PostMapping("/load")
    public ResponseEntity<Map<String, Object>> loadDrone(@RequestBody Map<String, Object> requestBody) {
        try {
            String serialNumber = (String) requestBody.get("serialNumber");
            Integer medicationId = (Integer) requestBody.get("medicationId");
            Integer quantity = (Integer) requestBody.get("quantity");

            droneService.loadDrone(serialNumber, medicationId, quantity);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Drone loaded successfully");
            response.put("serialNumber", serialNumber);
            response.put("medicationId", medicationId);
            response.put("quantity", quantity);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return handleErrorResponse(e);
        }
    }

    // Check drone availability for loading
    @GetMapping("/available")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

    // Checking loaded medications for a given drone
    @GetMapping("/medications")
    public ResponseEntity<List<DeliveryList>> getLoadedMedications(@RequestParam String serialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(serialNumber));
    }

    // Check drone information (Battery)
    @GetMapping("/battery")
    public ResponseEntity<Map<String, Object>> getDroneBattery(@RequestParam String serialNumber) {
        try {
            int batteryPercentage = droneService.getDroneBatteryLevel(serialNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("serialNumber", serialNumber);
            response.put("batteryPercentage", batteryPercentage);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return handleErrorResponse(e);
        }
    }

    // Change drone status to "DELIVERING" if it is in LOADED or LOADING state
    @PostMapping("/placeDelivery")
    public ResponseEntity<Map<String, Object>> placeDelivery(@RequestBody Map<String, Object> requestBody) {
        String serialNumber = (String) requestBody.get("serialNumber");

        Drone drone = droneService.getDroneInfo(serialNumber);
        if (drone.getDroneState() == DroneState.LOADED || drone.getDroneState() == DroneState.LOADING) {
            droneService.updateDroneState(serialNumber, DroneState.DELIVERING);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Drone is now delivering");
            response.put("serialNumber", serialNumber);

            return ResponseEntity.ok(response);
        }

        return createErrorResponse("Drone state must be LOADED or LOADING to proceed with delivery");
    }

    private ResponseEntity<Map<String, Object>> handleErrorResponse(RuntimeException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "failed");
        errorResponse.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
