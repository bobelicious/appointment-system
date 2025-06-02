package com.augusto.appointment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.appointment_system.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
