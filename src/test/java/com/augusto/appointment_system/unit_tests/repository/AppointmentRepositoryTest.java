package com.augusto.appointment_system.unit_tests.repository;

import static com.augusto.appointment_system.setup.SetupAppointment.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.repository.AppointmentRepository;

import jakarta.transaction.Transactional;

@DataJpaTest
public class AppointmentRepositoryTest {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    @Transactional
    public void givenAppointment_whenSave_thenReturnSavedAppointment() throws IOException {
        // when
        var appointment = appointmentRepository.save(appointment());

        // then - verify results
        assertThat(appointment).isNotNull();
        assertThat(appointment.getId()).isGreaterThan(0);
    }

    @Test
    public void givenStartTimeAndProfessionalEmail_whenExistsByStartTimeAndProfessionalEmail_thenReturnBool()
            throws IOException {
        // given
        appointmentRepository.save(appointment());

        // when
        var result = appointmentRepository.existsByStartTimeAndProfessionalEmail(appointment().getStartTime(),
                appointment().getProfessional().getEmail());

        // then
        assertThat(result).isTrue();
    }
}
