package com.drone.Repository;

import com.drone.Model.Drone;
import com.drone.Model.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    List<Drone> findByDroneState(DroneState state);
}
