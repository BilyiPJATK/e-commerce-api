package com.example.demo.controllers;

import com.example.demo.controllers.rental.MaintenanceLogController;
import com.example.demo.dtos.rental.request.MaintenanceLogRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.dtos.rental.response.MaintenanceLogResponseDto;
import com.example.demo.security.JwtService;
import com.example.demo.services.rental.MaintenanceLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MaintenanceLogController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MaintenanceLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private MaintenanceLogService maintenanceLogService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void createMaintenanceLog_ValidRequest_Returns201Created() throws Exception {
        MaintenanceLogRequestDto request = new MaintenanceLogRequestDto();
        request.setEquipmentId(1L);
        request.setDateSent(LocalDate.now());
        request.setDescription("Resoled toes");
        request.setCost(BigDecimal.valueOf(35.50));

        EquipmentResponseDto equipment = new EquipmentResponseDto();
        equipment.setId(1L);

        MaintenanceLogResponseDto response = new MaintenanceLogResponseDto();
        response.setId(1L);
        response.setEquipment(equipment);
        response.setDateSent(LocalDate.now());
        response.setDescription("Resoled toes");
        response.setCost(BigDecimal.valueOf(35.50));

        when(maintenanceLogService.createMaintenanceLog(any(MaintenanceLogRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/maintenance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.equipment.id").value(1))
                .andExpect(jsonPath("$.description").value("Resoled toes"))
                .andExpect(jsonPath("$.cost").value(35.5));
    }

    @Test
    void getMaintenanceLogById_ValidId_Returns200Ok() throws Exception {
        EquipmentResponseDto equipment = new EquipmentResponseDto();
        equipment.setId(1L);

        MaintenanceLogResponseDto response = new MaintenanceLogResponseDto();
        response.setId(1L);
        response.setEquipment(equipment);
        response.setDescription("Resoled toes");

        when(maintenanceLogService.getMaintenanceLogById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/maintenance/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.equipment.id").value(1))
                .andExpect(jsonPath("$.description").value("Resoled toes"));
    }

    @Test
    void getAllMaintenanceLogs_NoParams_Returns200Ok() throws Exception {
        EquipmentResponseDto equipment = new EquipmentResponseDto();
        equipment.setId(1L);

        MaintenanceLogResponseDto response = new MaintenanceLogResponseDto();
        response.setId(1L);
        response.setEquipment(equipment);
        response.setDescription("General checkup");

        when(maintenanceLogService.getAllMaintenanceLogs()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/maintenance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("General checkup"));
    }

    @Test
    void getMaintenanceLogs_WithEquipmentIdParam_Returns200Ok() throws Exception {
        EquipmentResponseDto equipment = new EquipmentResponseDto();
        equipment.setId(2L);

        MaintenanceLogResponseDto response = new MaintenanceLogResponseDto();
        response.setId(1L);
        response.setEquipment(equipment);
        response.setDescription("Strap replacement");

        when(maintenanceLogService.getLogsByEquipmentId(2L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/maintenance")
                        .param("equipmentId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].equipment.id").value(2))
                .andExpect(jsonPath("$[0].description").value("Strap replacement"));
    }

    @Test
    void deleteMaintenanceLog_ValidId_Returns204NoContent() throws Exception {
        doNothing().when(maintenanceLogService).deleteMaintenanceLog(1L);

        mockMvc.perform(delete("/api/maintenance/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}