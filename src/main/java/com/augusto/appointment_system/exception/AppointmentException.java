package com.augusto.appointment_system.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AppointmentException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
}
