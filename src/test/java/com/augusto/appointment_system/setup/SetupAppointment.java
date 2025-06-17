package com.augusto.appointment_system.setup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.model.Appointment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SetupAppointment {
    private static String APPOINTMENT_JSON_PATH = "src/test/resources/payload/appointment/entity/appointment.json";
    private static String APPOINTMENT_CLIENT_LIST_JSON_PATH = "src/test/resources/payload/appointment/entity/appointment-client-list.json";
    private static String APPOINTMENT_PROFESSIONAL_LIST_JSON_PATH = "src/test/resources/payload/appointment/entity/appointment-professional-list.json";
    private static String APPOINTMENT_DTO_JSON_PATH = "src/test/resources/payload/appointment/dto/appointment-dto.json";
    private static String APPOINTMENT_CLIENT_DTO_LIST_JSON_PATH = "src/test/resources/payload/appointment/dto/appointment-client-list-dto.json";
    private static String APPOINTMENT_PROFESSIONAL_DTO_LIST_JSON_PATH = "src/test/resources/payload/appointment/dto/appointment-professional-list-dto.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Appointment appointment() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_JSON_PATH),
                Appointment.class);
    }

    public static List<Appointment> appointmentClientList() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_CLIENT_LIST_JSON_PATH),
                new TypeReference<List<Appointment>>() {
                });
    }

    public static List<Appointment> appointmentProfessionalList() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_PROFESSIONAL_LIST_JSON_PATH),
                new TypeReference<List<Appointment>>() {
                });
    }

    public static AppointmentDto appointmentDto() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_DTO_JSON_PATH),
                AppointmentDto.class);
    }

    public static List<AppointmentDto> appointmentClientDtoList() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_CLIENT_DTO_LIST_JSON_PATH),
                new TypeReference<List<AppointmentDto>>() {
                });
    }

    public static List<AppointmentDto> appointmentProfessionalDtoList() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_PROFESSIONAL_DTO_LIST_JSON_PATH),
                new TypeReference<List<AppointmentDto>>() {
                });
    }
}
