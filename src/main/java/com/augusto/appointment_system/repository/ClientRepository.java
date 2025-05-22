package com.augusto.appointment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.appointment_system.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmail(String email);
}
