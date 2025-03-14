package com.drone.Service;

import com.drone.Model.Drone;
import com.drone.Model.DroneModel;
import com.drone.Model.DroneState;
import com.drone.Repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    //Registering a drone
    public void registerDrone(String serialNumber, DroneModel droneModel,
                              int weightLimit, int batteryCapacity, DroneState droneState) {
    }

    //Loading a drone with medication

    //Checking loaded medications for a given drone

    //Check drone availability for loading
    public List<Drone> getAvailableDrones() {
        return droneRepository.findByDroneState(DroneState.IDLE);
    }



}
