package com.augusto.appointment_system.unit_tests.repository;

import static com.augusto.appointment_system.setup.SetupAvailability.availability;
import static com.augusto.appointment_system.setup.SetupAvailability.updatedAvailability;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;

@DataJpaTest
class AvailabilityRepositoryTest {
    @Autowired
    AvailabilityRepository availabilityRepository;
    @Autowired
    ProfessionalRepository professionalRepository;

    @Test
    void givenAvailability_whenSaveAvailability_thenReturnAvailability() throws IOException {
        // given - precodition or setup

        // when - action or behavior we are going to test
        var savedAvailability = availabilityRepository.save(availability());

        // then - verify results
        assertThat(savedAvailability.getId()).isGreaterThan(0);
    }

    @Test
    void givenAvailability_whenUpdateAvailability_thenReturnUpdatedAvailability() throws IOException {
        // given - precodition or setup
        var availability = availabilityRepository.save(availability());
        availability = availabilityRepository.findById(availability.getId()).get();
        availability.setStartTime(updatedAvailability().getStartTime());
        availability.setEndTime(updatedAvailability().getEndTime());
        availability.setAvailableDays(updatedAvailability().getAvailableDays());
        // when - action or behavior we are going to test
        var updatedAvailability = availabilityRepository.save(availability);
        // then - verify results
        assertThat(updatedAvailability.getId()).isEqualTo(availability.getId());
        assertThat(updatedAvailability.getStartTime()).isEqualTo(availability.getStartTime());
        assertThat(updatedAvailability.getEndTime()).isEqualTo(availability.getEndTime());
        assertThat(updatedAvailability.getAvailableDays()).isEqualTo(availability.getAvailableDays());
    }

    @Test
    void givenProfessionalEmail_whenFindAvailabilityByProfessionalEmail_thenReturnAvailability() throws IOException {
        // given - setup or precodition
        professionalRepository.save(professional());
        availabilityRepository.save(availability());

        // when - codition or behavior we are going to test
        var availability = availabilityRepository.findAvailabilityByProfessionalEmail(professional().getEmail());

        // then - verify results
        assertThat(availability).isNotEmpty();

    }
}
