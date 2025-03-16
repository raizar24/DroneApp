package com.drone.Controller;

import com.drone.Model.Medication;
import com.drone.Service.MedicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MedicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private MedicationController medicationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(medicationController).build();
    }

    @Test
    void testAddMedication() throws Exception {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("Painkiller");
        medication.setWeight(10);
        medication.setCode("PK001");
        medication.setImage("image-url");

        when(medicationService.addMedication(any(Medication.class))).thenReturn(medication);

        mockMvc.perform(put("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Medication added or updated successfully"))
                .andExpect(jsonPath("$.medication.name").value("Painkiller"));
    }

    @Test
    void testGetMedications_NoId() throws Exception {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("Painkiller");

        when(medicationService.getAllMedications()).thenReturn(Collections.singletonList(medication));

        mockMvc.perform(get("/api/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Painkiller"));
    }

    @Test
    void testGetMedication_ById() throws Exception {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("Painkiller");

        when(medicationService.getMedicationById(1)).thenReturn(medication);

        mockMvc.perform(get("/api/medications?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.medication.name").value("Painkiller"));
    }

    @Test
    void testGetMedication_ById_NotFound() throws Exception {
        when(medicationService.getMedicationById(99)).thenThrow(new RuntimeException("Medication not found"));

        mockMvc.perform(get("/api/medications?id=99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Medication not found"));
    }

    @Test
    void testInvalidMedicationName() throws Exception {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("Invalid@Name");
        medication.setWeight(50);
        medication.setCode("VALIDCODE");
        medication.setImage("image.png");

        mockMvc.perform(put("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidMedicationCODE() throws Exception {
        Medication medication = new Medication();
        medication.setId(1);
        medication.setName("VALIDName");
        medication.setWeight(50);
        medication.setCode("INVALID@CODE");
        medication.setImage("image.png");

        mockMvc.perform(put("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isBadRequest());
    }


}
