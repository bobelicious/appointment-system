package com.augusto.appointment_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.appointment_system.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmail(String email);

    Optional<Client> findClientByEmail(String email);
}
