package com.augusto.appointment_system.mapper;

import com.augusto.appointment_system.dto.AvailabilityDto;
import com.augusto.appointment_system.model.Availability;

public class AvailabilityMapper {
    public static Availability mapToAvailability(AvailabilityDto availabilityDto) {
        return Availability.builder()
                .professionalEmail(availabilityDto.professionalEmail())
                .availableDays(availabilityDto.availableDays())
                .startTime(availabilityDto.startTime())
                .endTime(availabilityDto.endTime())
                .appointmentDurationMinutes(availabilityDto.appointMentDurations())
                .build();
    }

    public static AvailabilityDto mapToAvailabilityDto(Availability availability) {
        return new AvailabilityDto(
                availability.getProfessionalEmail(),
                availability.getAvailableDays(),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.getAppointmentDurationMinutes());
    }


}
