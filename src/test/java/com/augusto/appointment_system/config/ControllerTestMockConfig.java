package com.augusto.appointment_system.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.augusto.appointment_system.service.AppointmentService;
import com.augusto.appointment_system.service.AvailabilityService;
import com.augusto.appointment_system.service.ClientService;
import com.augusto.appointment_system.service.ProfessionalService;

@TestConfiguration
public class ControllerTestMockConfig {
    @MockitoBean
    private ClientService clientService;
    @MockitoBean
    private ProfessionalService professionalService;
    @MockitoBean
    private AppointmentService appointmentService;
    @MockitoBean
    private AvailabilityService availabilityService;

    @Bean
    ClientService clientService() {
        return Mockito.mock(ClientService.class);
    }

    @Bean
    AppointmentService appointmentService() {
        return Mockito.mock(AppointmentService.class);
    }

    @Bean
    ProfessionalService professionalService() {
        return Mockito.mock(ProfessionalService.class);
    }

    @Bean
    AvailabilityService availabilityService() {
        return Mockito.mock(AvailabilityService.class);
    }
}
