package com.augusto.appointment_system.unit_tests.controller;

import static com.augusto.appointment_system.setup.SetupAvailability.availabilityDto;
import static com.augusto.appointment_system.setup.SetupAvailability.updatedAvailabilityDto;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.augusto.appointment_system.dto.AvailabilityDto;
import com.augusto.appointment_system.service.AppointmentService;
import com.augusto.appointment_system.service.AvailabilityService;
import com.augusto.appointment_system.service.ClientService;
import com.augusto.appointment_system.service.ProfessionalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest
public class AvailabilityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ClientService clientService;
    @MockitoBean
    private ProfessionalService professionalService;
    @MockitoBean
    private AppointmentService appointmentService;
    @MockitoBean
    private AvailabilityService availabilityService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void givenAvailabilityDto_whenSave_thenReturnAvailabilityDto() throws Exception {
        // given - precodition or setup
        given(availabilityService.saveAvailability(any(AvailabilityDto.class))).willReturn(availabilityDto());
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
        given(availabilityService.updateAvailabilityDto(anyString(), any(AvailabilityDto.class)))
                .willReturn(updatedAvailabilityDto());
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
