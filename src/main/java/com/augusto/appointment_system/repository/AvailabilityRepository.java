package com.augusto.appointment_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.appointment_system.model.Availability;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Optional<Availability> findAvailabilityByProfessionalEmail(String professionalEmail);

}
