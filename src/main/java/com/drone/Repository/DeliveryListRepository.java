package com.drone.Repository;

import com.drone.Model.DeliveryList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeliveryListRepository extends JpaRepository<DeliveryList, Long> {
    List<DeliveryList> findByDroneSerialNumber(String serialNumber);
}
