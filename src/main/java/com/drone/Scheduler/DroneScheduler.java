package com.drone.Scheduler;

import com.drone.Model.DeliveryList;
import com.drone.Model.Drone;
import com.drone.Model.DroneState;
import com.drone.Repository.DeliveryListRepository;
import com.drone.Repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class DroneScheduler {

    private static final int BATTERY_THRESHOLD = 25;
    private static final int BATTERY_CONSUMPTION_PER_TRIP = 25;
    private static final int SCHEDULE_INTERVAL_MS = 10000; // 10 seconds

    private static final Logger logger = LoggerFactory.getLogger(DroneScheduler.class);

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DeliveryListRepository deliveryListRepository;

    @Scheduled(fixedRate = SCHEDULE_INTERVAL_MS)
    public void manageDroneStates() {
        List<Drone> drones = droneRepository.findAll();

        for (Drone drone : drones) {
            DroneState currentState = drone.getDroneState();
            String serialNumber = drone.getSerialNumber();

            switch (currentState) {
                case IDLE:
                    if (drone.getBatteryCapacity() < BATTERY_THRESHOLD) {
                        logger.warn("Drone {} battery too low for loading. Current: {}%", serialNumber, drone.getBatteryCapacity());
                    } else {
                        logger.info("Drone {} is available for new deliveries.", serialNumber);
                    }
                    break;

                case LOADING:
                    logger.info("Drone {} is in LOADING state.", serialNumber);
                    break;

                case LOADED:
                    updateDroneState(drone, DroneState.DELIVERING);
                    logger.info("Drone {} is now transitioning to DELIVERY state.", serialNumber);
                    break;

                case DELIVERING:
                    logger.info("Drone {} is on the way. DELIVERING.", serialNumber);
                    completeDelivery(drone);
                    break;

                case DELIVERED:
                    if (drone.getBatteryCapacity() >= BATTERY_CONSUMPTION_PER_TRIP) {
                        updateDroneState(drone, DroneState.RETURNING);
                        drone.setBatteryCapacity(drone.getBatteryCapacity() - BATTERY_CONSUMPTION_PER_TRIP);
                        logger.info("Drone {} is RETURNING. Battery now at {}%.", serialNumber, drone.getBatteryCapacity());
                    } else {
                        logger.warn("Drone {} cannot return due to low battery: {}%", serialNumber, drone.getBatteryCapacity());
                    }
                    break;

                case RETURNING:
                    updateDroneState(drone, DroneState.IDLE);
                    logger.info("Drone {} has returned and is now IDLE.", serialNumber);
                    break;

                default:
                    logger.error("Drone {} is in an unrecognized state: {}", serialNumber, currentState);
            }
        }
    }

    private void updateDroneState(Drone drone, DroneState newState) {
        if (drone.getDroneState() != newState) {
            drone.setDroneState(newState);
            droneRepository.save(drone);
        }
    }

    private void completeDelivery(Drone drone) {
        String serialNumber = drone.getSerialNumber();
        List<DeliveryList> deliveries = deliveryListRepository.findByDroneSerialNumber(serialNumber);

        if (!deliveries.isEmpty()) {
            deliveryListRepository.deleteAll(deliveries);
            logger.info("Drone {} has successfully delivered medications. Clearing delivery records.", serialNumber);
        }

        updateDroneState(drone, DroneState.DELIVERED);
    }
}
