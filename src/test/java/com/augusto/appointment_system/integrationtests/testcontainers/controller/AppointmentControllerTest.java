package com.augusto.appointment_system.integrationtests.testcontainers.controller;

import static com.augusto.appointment_system.setup.SetupAppointment.appointment;
import static com.augusto.appointment_system.setup.SetupAppointment.appointmentDto;
import static com.augusto.appointment_system.setup.SetupAvailability.availability;
import static com.augusto.appointment_system.setup.SetupClient.client;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class AppointmentControllerTest extends AbstractIntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProfessionalRepository professionalRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        appointmentRepository.deleteAll();
        clientRepository.deleteAll();
        professionalRepository.deleteAll();
        availabilityRepository.deleteAll();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void givenAppointmentDto_whenSaveAppointment_thenReturnAppointmentDto()
            throws JsonProcessingException, Exception {
        // given - precodition or setup
        clientRepository.save(client());
        professionalRepository.save(professional());
        availabilityRepository.save(availability());

        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(post("/api/v1/appointment/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentDto())));

        // then - verify
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientEmail",
                        is(appointmentDto().clientEmail())))
                .andExpect(jsonPath("$.professionalEmail",
                        is(appointmentDto().professionalEmail())));
    }

    @Test
    void givenAppointmentId_whenConfirmAppointment_thenReturnMessageConfirmation()
            throws JsonProcessingException, Exception {
        // given - precodition or setup
        var appointment = appointment();
        appointment.setProfessional(professionalRepository.save(professional()));
        appointment.setClient(clientRepository.save(client()));
        availabilityRepository.save(availability());
        appointmentRepository.save(appointment);
        // when - action or behaviour that we are goint test
        var result = mockMvc.perform(get("/api/v1/appointment/confirm/{id}", appointment.getId()));

        // then - verify
        result.andExpect(status().isOk());
    }

}
