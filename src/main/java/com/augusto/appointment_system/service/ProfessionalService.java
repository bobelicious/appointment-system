package com.augusto.appointment_system.service;

import static com.augusto.appointment_system.mapper.ProfessionalMapper.mapToProfessional;
import static com.augusto.appointment_system.mapper.ProfessionalMapper.mapToProfessionalDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.appointment_system.dto.ProfessionalDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.mapper.ProfessionalMapper;
import com.augusto.appointment_system.repository.ProfessionalRepository;

@Service
public class ProfessionalService {

    @Autowired
    private ProfessionalRepository professionalRepository;

    public ProfessionalDto saveProfessional(ProfessionalDto professionalDto) {
        validateProfessionalEmail(professionalDto.email());
        var professional = professionalRepository.save(mapToProfessional(professionalDto));
        return mapToProfessionalDto(professional);
    }

    public ProfessionalDto findById(Long id) {
        var professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional", "id", id));
        return mapToProfessionalDto(professional);
    }

    public List<ProfessionalDto> findAll() {
        return professionalRepository.findAll().stream()
                .map(ProfessionalMapper::mapToProfessionalDto)
                .toList();
    }

    public ProfessionalDto updateProfessional(ProfessionalDto professionalDto, Long id) {
        validateProfessionalEmail(professionalDto.email());
        var professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional", "id", id));
        professional = mapToProfessional(professionalDto);
        professional.setId(id);
        return mapToProfessionalDto(professionalRepository.save(professional));
    }

    public void deleteProfessional(Long id) {
        professionalRepository.findById(id)
                .ifPresentOrElse(professionalRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Professional", "id", id);
                        });
        ;
    }

    private void validateProfessionalEmail(String email) {
        if (professionalRepository.existsByEmail(email)) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
    }
}
