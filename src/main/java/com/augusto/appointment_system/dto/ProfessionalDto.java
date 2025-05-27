package com.augusto.appointment_system.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfessionalDto(

        @NotBlank(message = "Name is mandatory") 
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") 
        String name,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Specialty is mandatory")
        @Size(min = 2, max = 100, message = "Specialty must be between 2 and 100 characters")
        @Column(nullable = false)
        String specialty
    ) {

}
