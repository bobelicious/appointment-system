package com.augusto.appointment_system.mapper;

import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.model.Appointment;
import com.augusto.appointment_system.model.Client;
import com.augusto.appointment_system.model.Professional;

public class AppointmentMapper {
    public static Appointment mapToAppointment(AppointmentDto appointmentDto, Client client,
            Professional professional) {
        return Appointment.builder()
                .client(client)
                .professional(professional)
                .startTime(appointmentDto.startTime())
                .endTime(appointmentDto.endTime())
                .build();
    }

    public static AppointmentDto mapToAppointmentDto(Appointment appointment) {
        return new AppointmentDto(appointment.getClient().getEmail(), appointment.getProfessional().getEmail(),
                appointment.getStartTime(), appointment.getEndTime(), appointment.getStatus());
    }
}
