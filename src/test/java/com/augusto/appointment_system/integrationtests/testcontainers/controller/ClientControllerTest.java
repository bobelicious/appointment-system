package com.augusto.appointment_system.integrationtests.testcontainers.controller;

import static com.augusto.appointment_system.setup.SetupClient.client;
import static com.augusto.appointment_system.setup.SetupClient.clientDto;
import static com.augusto.appointment_system.setup.SetupClient.clientDtoList;
import static com.augusto.appointment_system.setup.SetupClient.clientList;
import static com.augusto.appointment_system.setup.SetupClient.updatedClientDto;
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

import com.augusto.appointment_system.integrationtests.testcontainers.AbstractIntegrationTest;
import com.augusto.appointment_system.repository.AppointmentRepository;
import com.augusto.appointment_system.repository.ClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class ClientControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        appointmentRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void givenClientDto_whenSaveClient_thenReturnClientDto() throws JsonProcessingException, Exception {
        // given - precodition or setup

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(post("/api/v1/client/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDto())));

        // then - verify
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(clientDto().getName())))
                .andExpect(jsonPath("$.email",
                        is(clientDto().getEmail())));
    }

    @Test
    void givenClientId_whenFindById_thenReturnClientDto() throws JsonProcessingException, Exception {
        // given - precodition or setup
        var client = clientRepository.save(client());

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(get("/api/v1/client/{id}", client.getId()));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(clientDto().getName())))
                .andExpect(jsonPath("$.email", is(clientDto().getEmail())));
    }

    @Test
    void givenClientList_whenFindAll_thenReturnListOfClientDto() throws Exception {
        // given - precodition or setup
        clientRepository.saveAll(clientList());

        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(get("/api/v1/client/list-all"));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(clientDtoList().size())));
    }

    @Test
    void givenClientDto_whenUpdate_thenReturnClientDto() throws JsonProcessingException, Exception {
        // given - precodition or setup
        var client = clientRepository.save(client());

        // when - action or behaviour that we are goint to test
        var result = mockMvc.perform(
                put("/api/v1/client/update/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClientDto())));

        // then - verify result
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedClientDto().getName())))
                .andExpect(jsonPath("$.email", is(updatedClientDto().getEmail())))
                .andExpect(jsonPath("$.phone", is(updatedClientDto().getPhone())));

    }

    @Test
    void givenClientId_whenDeleteClient_thenVerify() throws Exception {
        // given - precodition or setup
        var client = clientRepository.save(client());
        // when - action or the behaviour that we are goin test
        var result = mockMvc.perform(delete("/api/v1/client/delete/{id}", client.getId()));

        // then - verify
        result.andExpect(status().isNoContent());
    }

}
