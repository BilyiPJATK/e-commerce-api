package com.example.demo.controllers;

import com.example.demo.controllers.rental.EquipmentController;
import com.example.demo.dtos.rental.request.EquipmentRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import com.example.demo.security.JwtService;
import com.example.demo.services.rental.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private EquipmentService equipmentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void addEquipment_ValidRequest_Returns201Created() throws Exception {
        EquipmentRequestDto request = new EquipmentRequestDto();
        request.setBrand("BrandX");
        request.setModel("ModelY");
        request.setSize("42");
        request.setSku("BX-MY-42");
        request.setPurchaseDate(LocalDate.now());
        request.setType(EquipmentType.RENTAL_SHOE);
        request.setCondition(EquipmentCondition.NEW);

        EquipmentResponseDto response = new EquipmentResponseDto();
        response.setId(1L);
        response.setBrand("BrandX");
        response.setModel("ModelY");
        response.setType(EquipmentType.RENTAL_SHOE);
        response.setCondition(EquipmentCondition.NEW);

        when(equipmentService.addEquipment(any(EquipmentRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("BrandX"))
                .andExpect(jsonPath("$.model").value("ModelY"));
    }

    @Test
    void getEquipmentById_ValidId_Returns200Ok() throws Exception {
        EquipmentResponseDto response = new EquipmentResponseDto();
        response.setId(1L);
        response.setBrand("BrandX");
        response.setModel("ModelY");
        response.setType(EquipmentType.RENTAL_SHOE);
        response.setCondition(EquipmentCondition.NEW);

        when(equipmentService.getEquipmentById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/equipment/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("BrandX"));
    }

    @Test
    void getAllEquipment_NoParams_Returns200Ok() throws Exception {
        EquipmentResponseDto response = new EquipmentResponseDto();
        response.setId(1L);
        response.setBrand("BrandX");

        when(equipmentService.getAllEquipment()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/equipment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getAllEquipment_WithTypeParam_Returns200Ok() throws Exception {
        EquipmentResponseDto response = new EquipmentResponseDto();
        response.setId(1L);
        response.setType(EquipmentType.RENTAL_SHOE);

        when(equipmentService.getEquipmentByType(EquipmentType.RENTAL_SHOE)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/equipment")
                        .param("type", "RENTAL_SHOE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].type").value("RENTAL_SHOE"));
    }

    @Test
    void getAllEquipment_WithConditionParam_Returns200Ok() throws Exception {
        EquipmentResponseDto response = new EquipmentResponseDto();
        response.setId(1L);
        response.setCondition(EquipmentCondition.NEEDS_REPAIR);

        when(equipmentService.getEquipmentByCondition(EquipmentCondition.NEEDS_REPAIR)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/equipment")
                        .param("condition", "NEEDS_REPAIR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].condition").value("NEEDS_REPAIR"));
    }

    @Test
    void updateEquipmentCondition_ValidRequest_Returns200Ok() throws Exception {
        EquipmentResponseDto response = new EquipmentResponseDto();
        response.setId(1L);
        response.setCondition(EquipmentCondition.NEEDS_REPAIR);

        when(equipmentService.updateEquipmentCondition(1L, EquipmentCondition.NEEDS_REPAIR)).thenReturn(response);

        mockMvc.perform(patch("/api/equipment/{id}/condition", 1L)
                        .param("newCondition", "NEEDS_REPAIR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.condition").value("NEEDS_REPAIR"));
    }

    @Test
    void retireEquipment_ValidRequest_Returns204NoContent() throws Exception {
        doNothing().when(equipmentService).retireEquipment(1L);

        mockMvc.perform(patch("/api/equipment/{id}/retire", 1L))
                .andExpect(status().isNoContent());
    }
}