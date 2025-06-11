package com.augusto.appointment_system.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record AvailabilityDto(
        String professionalEmail,
        List<DayOfWeek> availableDays,
        LocalTime startTime,
        LocalTime endTime,
        Integer appointMentDurations) {

}
