package com.augusto.appointment_system.unit_tests.service;

import static com.augusto.appointment_system.setup.SetupAppointment.appointment;
import static com.augusto.appointment_system.setup.SetupAppointment.appointmentDto;
import static com.augusto.appointment_system.setup.SetupAvailability.availability;
import static com.augusto.appointment_system.setup.SetupClient.client;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import com.augusto.appointment_system.config.MailConfig;
import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.model.Appointment;
import com.augusto.appointment_system.repository.AppointmentRepository;
import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.augusto.appointment_system.service.AppointmentService;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ProfessionalRepository professionalRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;
    @Mock
    private MailConfig mailConfig;
    @Mock
    private Environment environment;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenReturnAppointmentDto() throws IOException {
        // given - precodition or setup
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(clientRepository.findClientByEmail(appointmentDto().clientEmail()))
                .willReturn(Optional.of(client()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.of(availability()));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(appointment());

        // when - action or the behavior that we are going to test
        var savedAppointment = appointmentService.createAppointment(appointmentDto());

        // then - verify the results
        assertThat(savedAppointment).isNotNull();
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowAvailabilityNotFound() throws IOException {
        // given - precodition or setup
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.ofNullable(null));

        // when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentDto());
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowUnvaliableDay() throws IOException {
        // given - precodition or setup
        var wrongAppointmentDto = new AppointmentDto(
                "pepiye3978@claspira.com",
                "a@gmail.com",
                LocalDateTime.of(2025, 06, 14, 10, 00),
                LocalDateTime.of(2025, 06, 14, 10, 45));

        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.of(availability()));

        // when - action or the behavior that we are going to test
        assertThrows(AppointmentException.class, () -> {
            appointmentService.createAppointment(wrongAppointmentDto);
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowUnvaliableTime() throws IOException {
        // given - precodition or setup
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.of(availability()));
        given(appointmentRepository.existsByStartTimeAndProfessionalEmail(appointmentDto().startTime(),
                appointmentDto().professionalEmail())).willReturn(true);
        // when - action or the behavior that we are going to test
        assertThrows(AppointmentException.class, () -> {
            appointmentService.createAppointment(appointmentDto());
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowsInvalidDate() throws IOException {
        // given - precodition or setup\
        var wrongAppointmentDto = new AppointmentDto(
                "a@email.com",
                "a@gmail.com",
                LocalDateTime.of(2025, 06, 9, 10, 00),
                LocalDateTime.of(2025, 06, 9, 10, 45));

        // when - action or the behavior that we are going to test
        assertThrows(AppointmentException.class, () -> {
            appointmentService.createAppointment(wrongAppointmentDto);
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowsStartTimeIsBefore() throws IOException {
        // given - precodition or setup\
        var wrongAppointmentDto = new AppointmentDto(
                "a@email.com",
                "a@gmail.com",
                LocalDateTime.of(2025, 06, 12, 05, 00),
                LocalDateTime.of(2025, 06, 12, 10, 45));
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.of(availability()));

        // when - action or the behavior that we are going to test
        assertThrows(AppointmentException.class, () -> {
            appointmentService.createAppointment(wrongAppointmentDto);
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowsStartTimeIsAfter() throws IOException {
        // given - precodition or setup\
        var wrongAppointmentDto = new AppointmentDto(
                "a@email.com",
                "a@gmail.com",
                LocalDateTime.of(2025, 06, 12, 19, 00),
                LocalDateTime.of(2025, 06, 12, 10, 45));
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.of(availability()));

        // when - action or the behavior that we are going to test
        assertThrows(AppointmentException.class, () -> {
            appointmentService.createAppointment(wrongAppointmentDto);
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowsClientNotFound() throws IOException {
        // given - precodition or setup
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willReturn(Optional.of(professional()));
        given(availabilityRepository
                .findAvailabilityByProfessionalEmail(appointment().getProfessional().getEmail()))
                .willReturn(Optional.of(availability()));
        given(clientRepository.findClientByEmail(appointmentDto().clientEmail()))
                .willReturn(Optional.ofNullable(null));

        // when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentDto());
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentDto_whenSaveAppointment_thenThrowsProfessionalNotFound() throws IOException {
        // given - precodition or setup
        given(professionalRepository.findProfessionalByEmail(appointmentDto().professionalEmail()))
                .willThrow(ResourceNotFoundException.class);

        // when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentDto());
        });

        // then - verify the results
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    public void givenAppointmentId_whenChangeAppointmentToConfirmed_thenReturnMessage() throws IOException {
        // given - precodition or setup
        given(appointmentRepository.findById(1L)).willReturn(Optional.of(appointment()));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(appointment());
        // when - action or the behavior that we are going to test
        var result = appointmentService.confirmAppointment(1L);

        // then - verify the results
        assertThat(result).isEqualTo("Status confirmed successful");
    }
}
