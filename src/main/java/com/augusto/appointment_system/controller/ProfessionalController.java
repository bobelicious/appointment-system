package com.augusto.appointment_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.appointment_system.dto.ProfessionalDto;
import com.augusto.appointment_system.service.ProfessionalService;

@RestController
@RequestMapping("/api/v1/professional")
public class ProfessionalController {
    @Autowired
    private ProfessionalService professionalService;

    @PostMapping("/new")
    public ResponseEntity<ProfessionalDto> createProfessional(@RequestBody ProfessionalDto professional) {
        return new ResponseEntity<>(professionalService.saveProfessional(professional), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalDto> findProfessionalById(@PathVariable Long id) {
        return new ResponseEntity<>(professionalService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/list-all")
    public ResponseEntity<List<ProfessionalDto>> findAll() {
        return new ResponseEntity<>(professionalService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProfessionalDto> updateProfessional(@PathVariable Long id,
            @RequestBody ProfessionalDto professionalDto) {
        return new ResponseEntity<>(professionalService.updateProfessional(professionalDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProfessional(@PathVariable Long id) {
        professionalService.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }
}
