package com.augusto.appointment_system.dto;

import java.time.LocalDateTime;

public record AppointmentDto(
        String clientEmail,
        String professionalEmail,
        LocalDateTime startTime,
        LocalDateTime endTime) {

}