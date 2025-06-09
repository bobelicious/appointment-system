package com.augusto.appointment_system.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "availabilities")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "professional_email", referencedColumnName = "email", nullable = false)
    private String professionalEmail;

    @ElementCollection
    @CollectionTable(name = "available_days", joinColumns = @JoinColumn(name = "availability_id"))
    private List<DayOfWeek> availableDays;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer appointmentDurationMinutes;
}