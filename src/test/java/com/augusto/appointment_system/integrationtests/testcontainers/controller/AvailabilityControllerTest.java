package com.augusto.appointment_system.integrationtests.testcontainers.controller;

import static com.augusto.appointment_system.setup.SetupAvailability.availability;
import static com.augusto.appointment_system.setup.SetupAvailability.availabilityDto;
import static com.augusto.appointment_system.setup.SetupAvailability.updatedAvailabilityDto;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.augusto.appointment_system.integrationtests.testcontainers.AbstractIntegrationTest;
import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class AvailabilityControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProfessionalRepository professionalRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        professionalRepository.deleteAll();
        availabilityRepository.deleteAll();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void givenAvailabilityDto_whenSave_thenReturnAvailabilityDto() throws Exception {
        // given - precodition or setup
        professionalRepository.save(professional());
        // when - action or behavior we are going to test
        var result = mockMvc.perform(post("/api/v1/availability/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(availabilityDto())));
        // then - verify results
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.professionalEmail",
                        is(availabilityDto().professionalEmail())));
    }

    @Test
    void givenAvailabilityDto_whenUpdate_thenReturnUpdatedAvailabilityDto() throws Exception {
        // given - precodition or setup
        professionalRepository.save(professional());
        availabilityRepository.save(availability());
        // when - action or behavior we are going to test
        var result = mockMvc.perform(put("/api/v1/availability/update")
                .param("professionalEmail", professional().getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAvailabilityDto())));
        // then - verify results
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.professionalEmail",
                        is(updatedAvailabilityDto().professionalEmail())))
                .andExpect(jsonPath("$.availableDays.size()", is(updatedAvailabilityDto().availableDays().size())));
    }

}
