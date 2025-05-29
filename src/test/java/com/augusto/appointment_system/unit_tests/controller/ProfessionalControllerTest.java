package com.augusto.appointment_system.unit_tests.controller;

import static com.augusto.appointment_system.setup.SetupProfessional.professionalDto;
import static com.augusto.appointment_system.setup.SetupProfessional.professionalDtoList;
import static com.augusto.appointment_system.setup.SetupProfessional.updatedProfessionalDto;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.augusto.appointment_system.dto.ProfessionalDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.service.ClientService;
import com.augusto.appointment_system.service.ProfessionalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class ProfessionalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;
    @MockitoBean
    private ProfessionalService professionalService;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void givenProfessionalDto_whenSaveProfessional_thenReturnProfessionalDto()
            throws JsonProcessingException, Exception {
        // given - precodition or setup
        given(professionalService.saveProfessional(any(ProfessionalDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(post("/api/v1/professional/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(professionalDto())));

        // then - verify
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(professionalDto().name())))
                .andExpect(jsonPath("$.email",
                        is(professionalDto().email())))
                .andExpect(jsonPath("$.specialty",
                        is(professionalDto().specialty())));
    }

    @Test
    void givenProfessionalDto_whenSaveProfessional_thenThrowsAppointmentException()
            throws JsonProcessingException, Exception {
        // given - precodition or setup
        given(professionalService.saveProfessional(any(ProfessionalDto.class)))
                .willThrow(AppointmentException.class);

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(post("/api/v1/professional/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(professionalDto())));

        // then - verify
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenProfessionalId_whenFindById_thenReturnProfessionalDto() throws JsonProcessingException, Exception {
        // given - precodition or setup
        given(professionalService.findById(1L))
                .willReturn(professionalDto());
        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(get("/api/v1/professional/{id}", 1L));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(professionalDto().name())))
                .andExpect(jsonPath("$.email", is(professionalDto().email())))
                .andExpect(jsonPath("$.specialty", is(professionalDto().specialty())));
    }

    @Test
    void givenProfessionalList_whenFindAll_thenReturnListOfProfessionalDto() throws Exception {
        // given - precodition or setup
        given(professionalService.findAll())
                .willReturn(professionalDtoList());
        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(get("/api/v1/professional/list-all"));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(professionalDtoList().size())));
    }

    @Test
    void givenProfessionalDto_whenUpdate_thenReturnProfessionalDto() throws JsonProcessingException, Exception {
        // given - precodition or setup

        given(professionalService.updateProfessional(any(ProfessionalDto.class), any(Long.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(
                put("/api/v1/professional/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProfessionalDto())));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedProfessionalDto().name())))
                .andExpect(jsonPath("$.email", is(updatedProfessionalDto().email())))
                .andExpect(jsonPath("$.specialty", is(updatedProfessionalDto().specialty())));

    }

    @Test
    void givenProfessionalDto_whenUpdate_thenThrowsRetunrNotFoundException() throws JsonProcessingException, Exception {
        // given - precodition or setup
        given(professionalService.updateProfessional(any(ProfessionalDto.class), any(Long.class)))
                .willThrow(ResourceNotFoundException.class);
        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(
                put("/api/v1/professional/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProfessionalDto())));

        // then - verify result
        result.andExpect(status().isNotFound());
    }

    @Test
    void givenProfessionalDto_whenUpdate_thenThrowsAppointmentException() throws JsonProcessingException, Exception {
        // given - precodition or setup
        given(professionalService.updateProfessional(any(ProfessionalDto.class), any(Long.class)))
                .willThrow(AppointmentException.class);
        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(
                put("/api/v1/professional/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProfessionalDto())));

        // then - verify result
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenProfessionalId_whenDeleteProfessional_thenVerify() throws Exception {
        // given - precodition or setup
        willDoNothing().given(professionalService).deleteProfessional(1L);
        
        // when - action or the behaviour that we are goin test
        var result = mockMvc.perform(delete("/api/v1/professional/delete/{id}", 1L));

        // then - verify
        result.andExpect(status().isNoContent());
    }

    @Test
    void givenProfessionalId_whenDeleteProfessional_thenThrowsResourceNotFoundException() throws Exception {
        // given - precodition or setup
        willThrow(ResourceNotFoundException.class)
                .given(professionalService)
                .deleteProfessional(1L);
        // when - action or the behaviour that we are goin test
        var result = mockMvc.perform(delete("/api/v1/professional/delete/{id}", 1L));

        // then - verify
        result.andExpect(status().isNotFound());
    }
}
