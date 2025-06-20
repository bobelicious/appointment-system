package com.augusto.appointment_system.unit_tests.controller;

import static com.augusto.appointment_system.setup.SetupAppointment.appointmentClientDtoList;
import static com.augusto.appointment_system.setup.SetupAppointment.appointmentDto;
import static com.augusto.appointment_system.setup.SetupAppointment.appointmentProfessionalDtoList;
import static com.augusto.appointment_system.setup.SetupClient.clientDto;
import static com.augusto.appointment_system.setup.SetupProfessional.professionalDto;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.service.AppointmentService;
import com.augusto.appointment_system.service.AvailabilityService;
import com.augusto.appointment_system.service.ClientService;
import com.augusto.appointment_system.service.ProfessionalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest
class AppointmentControllerTest {
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
    void givenAppointmentDto_whenSaveAppointment_thenReturnAppointmentDto() throws Exception {
        // given - precodition or setup
        given(appointmentService.createAppointment(any(AppointmentDto.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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
            throws Exception {
        // given - precodition or setup
        given(appointmentService.confirmAppointment(1L)).willReturn("Status confirmed successful");

        // when - action or behaviour that we are going test
        var result = mockMvc.perform(get("/api/v1/appointment/confirm/{id}", 1L));

        // then - verify
        result.andExpect(status().isOk());
    }

    @Test
    void givenClientEmail_whenFindAllClientAppointments_thenReturnListClientScheduledAppointments() throws Exception {
        // given - precodition or setup
        given(appointmentService.listClientScheduledAppointments(clientDto().getEmail()))
                .willReturn(appointmentClientDtoList());

        // when - action or behaviour that we are going to test
        var result = mockMvc
                .perform(get("/api/v1/appointment/scheduled-appointments/client/{email}", clientDto().getEmail()));

        // then - verify
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(appointmentClientDtoList().size())));

    }

    @Test
    void givenProfessionalEmail_whenFindAllProfessionalAppointments_thenRetunrListProfessionalScheduledAppointments()
            throws Exception {
        // given - precodition or setup
        given(appointmentService.listProfessionalScheduledAppointments(professionalDto().email()))
                .willReturn(appointmentProfessionalDtoList());
        // when - actiuon or behaviour that we are going to test
        var result = mockMvc.perform(
                get("/api/v1/appointment/scheduled-appointments/professional/{email}", professionalDto().email()));

        // then - verify
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(appointmentProfessionalDtoList().size())));
    }

    @Test
    void givenAppointmentId_whenChangeAppointmentToCancel_returnCancelMsg() throws Exception {
        // given - precodition or setup
        given(appointmentService.cancelAppointment(1L)).willReturn("Status canceled successful");

        // when - action or behaviour we are going to test
        var result = mockMvc.perform(get("/api/v1/appointment/cancel/{id}", 1L));

        // then - verify
        result.andExpect(status().isOk()).andExpect(jsonPath("$", is("Status canceled successful")));
    }
}
