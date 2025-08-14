package com.augusto.appointment_system.unit_tests.service;

import static com.augusto.appointment_system.setup.SetupAvailability.availability;
import static com.augusto.appointment_system.setup.SetupAvailability.availabilityDto;
import static com.augusto.appointment_system.setup.SetupAvailability.updatedAvailability;
import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.model.Availability;
import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.augusto.appointment_system.service.AvailabilityService;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {
    @Mock
    private ProfessionalRepository professionalRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    void givenAvailabilityDto_whenSave_thenReturnAvailabilityDto() throws IOException {
        // given - precodition or setup
        given(availabilityRepository.findAvailabilityByProfessionalEmail(professional().getEmail()))
                .willReturn(Optional.ofNullable(null));
        given(availabilityRepository.save(any(Availability.class))).willReturn(availability());
        // when - action or behavior we are going to test
        var result = availabilityService.saveAvailability(availabilityDto());
        // then - verify results
        assertThat(result.professionalEmail()).isEqualTo(professional().getEmail());
    }

    @Test
    void givenAvailabilityDto_whenSave_thenThrowsProfessionalHaveAvailabilityError() throws IOException {
        // given - precodition or setup
        given(availabilityRepository.findAvailabilityByProfessionalEmail(professional().getEmail()))
                .willReturn(Optional.of(availability()));
        // when - action or behavior we are going to test
        assertThrows(AppointmentException.class, () -> {
            availabilityService.saveAvailability(availabilityDto());
        });

    }

    @Test
    void givenAvailabilityDto_whenUpdate_thenReturnUpdatedAvailabilityDto() throws IOException {
        // given - precodition or setup
        given(availabilityRepository.findAvailabilityByProfessionalEmail(professional().getEmail()))
                .willReturn(Optional.of(availability()));
        given(availabilityRepository.save(any(Availability.class))).willReturn(updatedAvailability());
        // when - action or behavior we are going to test
        var result = availabilityService.updateAvailabilityDto(professional().getEmail(), availabilityDto());
        // then - verify results
        assertThat(result.professionalEmail()).isEqualTo(professional().getEmail());
        assertThat(result.availableDays()).isEqualTo(updatedAvailability().getAvailableDays());
        assertThat(result.endTime()).isEqualTo(updatedAvailability().getEndTime());
    }

    @Test
    void givenAvailabilityDto_whenUpdate_thenThrowsProfessionalNotFoundError() throws IOException {
        // given - precodition or setup
        given(availabilityRepository.findAvailabilityByProfessionalEmail(professional().getEmail()))
                .willReturn(Optional.ofNullable(null));
        // when - action or behavior we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            availabilityService.updateAvailabilityDto(professional().getEmail(), availabilityDto());
        });

    }
}
