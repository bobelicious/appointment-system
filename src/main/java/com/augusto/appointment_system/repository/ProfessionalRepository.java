package com.augusto.appointment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.appointment_system.model.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    boolean existsByEmail(String email);
}
