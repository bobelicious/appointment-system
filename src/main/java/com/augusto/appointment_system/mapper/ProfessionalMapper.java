package com.augusto.appointment_system.mapper;

import com.augusto.appointment_system.dto.ProfessionalDto;
import com.augusto.appointment_system.model.Professional;

public class ProfessionalMapper {
    public static ProfessionalDto mapToProfessionalDto(Professional professional) {
        return new ProfessionalDto(
                professional.getName(),
                professional.getEmail(),
                professional.getSpecialty()
        );
    }
    public static Professional mapToProfessional(ProfessionalDto professionalDto) {
        return Professional.builder()
                .name(professionalDto.name())
                .email(professionalDto.email())
                .specialty(professionalDto.specialty())
                .build();
    }
}
