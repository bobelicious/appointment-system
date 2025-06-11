package com.augusto.appointment_system.unit_tests.repository;

import static com.augusto.appointment_system.setup.SetupAppointment.appointment;
import static com.augusto.appointment_system.setup.SetupAvailability.availability;
import static com.augusto.appointment_system.setup.SetupClient.client;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.model.Appointment;
import com.augusto.appointment_system.repository.AppointmentRepository;
import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;

@DataJpaTest
public class AppointmentRepositoryTest {
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    ProfessionalRepository professionalRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;
    Appointment appointment;
    @BeforeEach
    void setup() throws IOException {
        appointment = appointment();
        appointment.setProfessional(professionalRepository.save(professional()));
        appointment.setClient(clientRepository.save(client()));
        availabilityRepository.save(availability());
    }

    @Test
    public void givenAppointment_whenSave_thenReturnSavedAppointment() throws IOException {
        // when
        appointmentRepository.save(appointment);

        // then - verify results
        assertThat(appointment).isNotNull();
        assertThat(appointment.getId()).isGreaterThan(0);
    }

    @Test
    public void givenStartTimeAndProfessionalEmail_whenExistsByStartTimeAndProfessionalEmail_thenReturnBool()
            throws IOException {
        // given
        appointmentRepository.save(appointment);

        // when
        var result = appointmentRepository.existsByStartTimeAndProfessionalEmail(appointment().getStartTime(),
                appointment().getProfessional().getEmail());

        // then
        assertThat(result).isTrue();
    }
}
