package com.society.management.controller;

import com.society.management.entity.Society;
import com.society.management.exception.DuplicateSocietyException;
import com.society.management.exception.GlobalExceptionHandler;
import com.society.management.exception.SocietyNotFoundException;
import com.society.management.service.SocietyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SocietyController.class)
@Import(GlobalExceptionHandler.class)
class SocietyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SocietyService societyService;

    @Test
    @DisplayName("GET /api/societies/{id} - Returns 404 Not Found when society does not exist")
    void getSocietyById_WhenNotFound_Returns404NotFound() throws Exception {
        when(societyService.getSocietyById(999L))
            .thenThrow(new SocietyNotFoundException("Society not found with id: 999"));

        mockMvc.perform(get("/api/societies/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Society not found with id: 999"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /api/societies - Returns 409 Conflict when registration number is duplicated")
    void createSociety_WhenDuplicateRegistrationNumber_Returns409Conflict() throws Exception {
        when(societyService.createSociety(any(Society.class)))
            .thenThrow(new DuplicateSocietyException("Society with registration number 'REG-001' already exists."));

        String validJson = """
            {
                "name": "Green Valley",
                "registrationNumber": "REG-001",
                "address": "123 Main St",
                "city": "Springfield",
                "state": "IL",
                "postalCode": "62701"
            }
            """;

        mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.error").value("Conflict"))
            .andExpect(jsonPath("$.message").value("Society with registration number 'REG-001' already exists."))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /api/societies - Returns 400 Bad Request when validation fails")
    void createSociety_WhenInvalidRequestData_Returns400BadRequest() throws Exception {
        String invalidJson = """
            {
                "name": "",
                "registrationNumber": "",
                "address": "",
                "city": "",
                "state": "",
                "postalCode": ""
            }
            """;

        mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /api/societies - Returns 201 Created when request data is valid")
    void createSociety_WhenValidRequest_Returns201Created() throws Exception {
        Society savedSociety = new Society(
            "Green Valley",
            "REG-001",
            "123 Main St",
            "Springfield",
            "IL",
            "62701"
        );
        savedSociety.setId(1L);
        savedSociety.setCreatedAt(OffsetDateTime.now());
        savedSociety.setUpdatedAt(OffsetDateTime.now());

        when(societyService.createSociety(any(Society.class))).thenReturn(savedSociety);

        String validJson = """
            {
                "name": "Green Valley",
                "registrationNumber": "REG-001",
                "address": "123 Main St",
                "city": "Springfield",
                "state": "IL",
                "postalCode": "62701"
            }
            """;

        mockMvc.perform(post("/api/societies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Green Valley"))
            .andExpect(jsonPath("$.registrationNumber").value("REG-001"))
            .andExpect(jsonPath("$.city").value("Springfield"));
    }

    @Test
    @DisplayName("GET /api/societies/{id} - Returns 200 OK when society exists")
    void getSocietyById_WhenFound_Returns200Ok() throws Exception {
        Society existingSociety = new Society(
            "Green Valley",
            "REG-001",
            "123 Main St",
            "Springfield",
            "IL",
            "62701"
        );
        existingSociety.setId(1L);
        existingSociety.setCreatedAt(OffsetDateTime.now());
        existingSociety.setUpdatedAt(OffsetDateTime.now());

        when(societyService.getSocietyById(1L)).thenReturn(existingSociety);

        mockMvc.perform(get("/api/societies/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Green Valley"))
            .andExpect(jsonPath("$.registrationNumber").value("REG-001"));
    }
}
