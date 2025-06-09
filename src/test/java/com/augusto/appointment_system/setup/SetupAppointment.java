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
    public static String APPOINTMENT_JSON_PATH = "src/test/resources/payload/appointment/entity/appointment.json";
    public static String APPOINTMENT_LIST_JSON_PATH = "src/test/resources/payload/appointment/entity/appointment-list.json";
    public static String APPOINTMENT_DTO_JSON_PATH = "src/test/resources/payload/appointment/dto/appointment-dto.json";
    public static String APPOINTMENT_LIST_DTO_JSON_PATH = "src/test/resources/payload/appointment/dto/appointment-list-dto.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Appointment appointment() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_JSON_PATH),
                Appointment.class);
    }

    public static List<Appointment> appointmentList() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_LIST_JSON_PATH),
                new TypeReference<List<Appointment>>() {
                });
    }

    public static AppointmentDto appointmentDto() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_DTO_JSON_PATH),
                AppointmentDto.class);
    }

    public static List<AppointmentDto> appointmentDtoList() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(APPOINTMENT_LIST_DTO_JSON_PATH),
                new TypeReference<List<AppointmentDto>>() {
                });
    }
}
