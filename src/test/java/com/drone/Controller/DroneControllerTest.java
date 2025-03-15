package com.drone.Controller;

import com.drone.Model.*;
import com.drone.Service.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;


    @InjectMocks
    private DroneController droneController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDrones() throws Exception {
        Drone drone = new Drone("DRONE001", DroneModel.LIGHTWEIGHT,
                100, 100, DroneState.IDLE);
        when(droneService.getAllDrones()).thenReturn(Collections.singletonList(drone));

        mockMvc.perform(get("/api/drones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("DRONE001"));
    }

    @Test
    void testRegisterDrone() throws Exception {
        Drone drone = new Drone("DRONE001", DroneModel.LIGHTWEIGHT,
                100, 100, DroneState.IDLE);
        doNothing().when(droneService).registerDrone(any(), any(), anyInt(), anyInt(), any());

        mockMvc.perform(post("/api/drones/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"));
    }


    @Test
    void testLoadDrone() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("serialNumber", "DRONE001");
        requestBody.put("medicationId", 1);
        requestBody.put("quantity", 10);

        doNothing().when(droneService).loadDrone("DRONE001", 1, 10);

        mockMvc.perform(post("/api/drones/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"));
    }

    @Test
    void testLoadDroneFailsIfOverloaded() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("serialNumber", "DRONE001");
        requestBody.put("medicationId", 1);
        requestBody.put("quantity", 1000); // Exceeds weight limit

        doThrow(new RuntimeException("Total weight exceeds the drone's capacity")).when(droneService).
                loadDrone("DRONE001", 1, 1000);

        mockMvc.perform(post("/api/drones/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Total weight exceeds the drone's capacity"));
    }

    @Test
    void testGetAvailableDrones() throws Exception {
        Drone drone = new Drone("DRONE001", DroneModel.LIGHTWEIGHT,
                500, 100, DroneState.IDLE);
        when(droneService.getAvailableDrones()).thenReturn(Collections.singletonList(drone));

        mockMvc.perform(get("/api/drones/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("DRONE001"));
    }

    @Test
    void testGetLoadedMedications() throws Exception {
        Medication medication = new Medication();
        medication.setName("Biogesic");
        medication.setWeight(10);
        medication.setCode("CODE001");
        medication.setImage("image1.jpg");

        DeliveryList delivery = new DeliveryList();
        delivery.setMedication(medication);
        when(droneService.getLoadedMedications("DRONE001"))
                .thenReturn(Collections.singletonList(delivery));

        mockMvc.perform(get("/api/drones/medications?serialNumber=DRONE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void testGetDroneBattery() throws Exception {
        when(droneService.getDroneBatteryLevel("DRONE001")).thenReturn(80);

        mockMvc.perform(get("/api/drones/battery?serialNumber=DRONE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryPercentage").value(80));
    }

    @Test
    void testPlaceDeliverySuccess() throws Exception {
        Drone drone = new Drone("DRONE001", DroneModel.LIGHTWEIGHT,
                500, 100, DroneState.LOADED);
        when(droneService.getDroneInfo("DRONE001")).thenReturn(drone);
        doNothing().when(droneService).updateDroneState("DRONE001", DroneState.DELIVERING);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("serialNumber", "DRONE001");

        mockMvc.perform(post("/api/drones/placeDelivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Drone is now delivering"));
    }

    @Test
    void testPlaceDeliveryFailure() throws Exception {
        Drone drone = new Drone("DRONE001", DroneModel.LIGHTWEIGHT,
                500, 100, DroneState.IDLE);
        when(droneService.getDroneInfo("DRONE001")).thenReturn(drone);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("serialNumber", "DRONE001");

        mockMvc.perform(post("/api/drones/placeDelivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failed"))
                .andExpect(jsonPath("$.message").value("Drone state must be LOADED or LOADING to proceed with delivery"));
    }
}
