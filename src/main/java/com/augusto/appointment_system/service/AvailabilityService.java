package com.augusto.appointment_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.appointment_system.dto.AvailabilityDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.model.Availability;

import static com.augusto.appointment_system.mapper.AvailabilityMapper.*;
import com.augusto.appointment_system.repository.AvailabilityRepository;

@Service
public class AvailabilityService {
    @Autowired
    private AvailabilityRepository availabilityRepository;

    public AvailabilityDto saveAvailability(AvailabilityDto availabilityDto) {
        if (availabilityRepository.findAvailabilityByProfessionalEmail(availabilityDto.professionalEmail())
                .isPresent()) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST,
                    "Professional who has availability, use the update endpoint");
        }
        var availability = mapToAvailability(availabilityDto);
        availability = availabilityRepository.save(availability);
        return mapToAvailabilityDto(availability);
    }

    public AvailabilityDto updateAvailabilityDto(String professionalEmail, AvailabilityDto availabilityDto) {
        var availability = availabilityRepository.findAvailabilityByProfessionalEmail(professionalEmail).orElseThrow(
                () -> new ResourceNotFoundException("Availability", "professional email", professionalEmail));
        availability = new Availability(
                availability.getId(),
                availabilityDto.professionalEmail(),
                availabilityDto.availableDays(),
                availabilityDto.startTime(),
                availabilityDto.endTime(),
                availabilityDto.appointMentDurations());
        availability = availabilityRepository.save(availability);
        return mapToAvailabilityDto(availability);
    }
}
