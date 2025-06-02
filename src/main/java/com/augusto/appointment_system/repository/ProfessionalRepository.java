package com.augusto.appointment_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.appointment_system.model.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    boolean existsByEmail(String email);
    Optional<Professional> findProfessionalByEmail(String email);
}
