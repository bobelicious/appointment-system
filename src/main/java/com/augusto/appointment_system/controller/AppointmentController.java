package com.augusto.appointment_system.controller;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/new")
    public ResponseEntity<AppointmentDto> postMethodName(@RequestBody AppointmentDto appointmentDto)
            throws UnknownHostException {
        return new ResponseEntity<>(appointmentService.createAppointment(appointmentDto), HttpStatus.CREATED);

    }

    @GetMapping("/confirm/{id}")
    public ResponseEntity<String> getMethodName(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.confirmAppointment(id), HttpStatus.OK);
    }

    @GetMapping("/scheduled-appointments/client/{email}")
    public ResponseEntity<List<AppointmentDto>> getMethodName(@PathVariable String email) {
        return new ResponseEntity<>(appointmentService.ListClientScheduledAppointments(email), HttpStatus.OK);
    }

}
