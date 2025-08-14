package com.augusto.appointment_system.dto;

import java.time.LocalDateTime;

import com.augusto.appointment_system.model.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.Future;

public record AppointmentDto(
        @JsonProperty(namespace = "client_email") String clientEmail,
        @JsonProperty(namespace = "professional_email") String professionalEmail,
        @JsonProperty(namespace = "sart_time") @Future LocalDateTime startTime,
        @JsonProperty(namespace = "end_time", access = Access.READ_ONLY) LocalDateTime endTime,
        @JsonProperty(namespace = "status", access = Access.READ_ONLY) AppointmentStatus status) {

}