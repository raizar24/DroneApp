package com.drone.Controller;

import com.drone.Model.Drone;
import com.drone.Service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    @RequestMapping("/register")
    public ResponseEntity<String> registerDrone(@RequestBody Drone drone){
        droneService.registerDrone(
                drone.getSerialNumber(),
                drone.getDroneModel(),
                drone.getWeightLimit(),
                drone.getBatteryCapacity(),
                drone.getDroneState()
        );
        return ResponseEntity.ok("Drone registered successfully");
    }

    @GetMapping("/available")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

}
