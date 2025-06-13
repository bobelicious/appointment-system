package com.augusto.appointment_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.appointment_system.dto.AvailabilityDto;
import com.augusto.appointment_system.service.AvailabilityService;
@RestController
@RequestMapping("/api/v1/availability")
public class AvailabilityController {
    @Autowired
    private AvailabilityService availabilityService;

    @PostMapping("/new")
    public ResponseEntity<AvailabilityDto> createAvaiilibility(@RequestBody AvailabilityDto availabilityDto) {
        return new ResponseEntity<>(availabilityService.saveAvailability(availabilityDto), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<AvailabilityDto> updateAvaiilibility(@RequestParam String professionalEmail,
            @RequestBody AvailabilityDto availabilityDto) {
        return new ResponseEntity<>(availabilityService.updateAvailabilityDto(professionalEmail, availabilityDto),
                HttpStatus.OK);
    }
}