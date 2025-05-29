package com.augusto.appointment_system.integrationtests.testcontainers;

import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static com.augusto.appointment_system.setup.SetupProfessional.professionalDto;
import static com.augusto.appointment_system.setup.SetupProfessional.professionalDtoList;
import static com.augusto.appointment_system.setup.SetupProfessional.professionalList;
import static com.augusto.appointment_system.setup.SetupProfessional.updatedProfessional;
import static com.augusto.appointment_system.setup.SetupProfessional.updatedProfessionalDto;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class ProfessionalControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProfessionalRepository professionalRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        professionalRepository.deleteAll();
    }

    @Test
    void givenProfessionalDto_whenSaveProfessional_thenReturnProfessionalDto()
            throws JsonProcessingException, Exception {
        // given - precodition or setup

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(post("/api/v1/professional/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(professional())));

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
        professionalRepository.save(professional());

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(post("/api/v1/professional/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString((professionalDto()))));

        // then - verify
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenProfessionalId_whenFindById_thenReturnProfessionalDto() throws JsonProcessingException, Exception {
        // given - precodition or setup
        var professional = professionalRepository.save(professional());

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(get("/api/v1/professional/{id}", professional.getId()));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(professionalDto().name())))
                .andExpect(jsonPath("$.email", is(professionalDto().email())))
                .andExpect(jsonPath("$.specialty", is(professionalDto().specialty())));
    }

    @Test
    void givenProfessionalList_whenFindAll_thenReturnListOfProfessionalDto() throws Exception {
        // given - precodition or setup
        professionalRepository.saveAll(professionalList());
        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(get("/api/v1/professional/list-all"));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(professionalDtoList().size())));
    }

    @Test
    void givenProfessionalDto_whenUpdate_thenReturnProfessionalDto() throws JsonProcessingException, Exception {
        // given - precodition or setup
        var professional = professionalRepository.save(professional());

        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(
                put("/api/v1/professional/update/{id}", professional.getId())
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
        var updatedProfessional = professionalRepository.save(updatedProfessional());
        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(
                put("/api/v1/professional/update/{id}", updatedProfessional.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProfessionalDto())));

        // then - verify result
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenProfessionalId_whenDeleteProfessional_thenVerify() throws Exception {
        // given - precodition or setup
        var professional = professionalRepository.save(professional());

        // when - action or the behaviour that we are goin test
        var result = mockMvc.perform(delete("/api/v1/professional/delete/{id}", professional.getId()));

        // then - verify
        result.andExpect(status().isNoContent());
    }

    @Test
    void givenProfessionalId_whenDeleteProfessional_thenThrowsResourceNotFoundException() throws Exception {
        // given - precodition or setup
        var professional = professionalRepository.save(professional());

        // when - action or the behaviour that we are goin test
        var result = mockMvc.perform(delete("/api/v1/professional/delete/{id}", professional.getId() + 1));

        // then - verify
        result.andExpect(status().isNotFound());
    }
}
