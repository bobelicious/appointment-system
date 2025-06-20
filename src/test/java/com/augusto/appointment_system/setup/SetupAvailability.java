package com.augusto.appointment_system.setup;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.augusto.appointment_system.dto.AvailabilityDto;
import com.augusto.appointment_system.model.Availability;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SetupAvailability {
    private static String AVAILABILITY_JSON_PATH = "src/test/resources/payload/availibility/entity/availibility.json";
    private static String AVAILABILITY_LIST_JSON_PATH = "src/test/resources/payload/availibility/entity/availability-list.json";
    private static String AVAILABILITY_DTO_JSON_PATH = "src/test/resources/payload/availibility/dto/availibility-dto.json";
    private static String AVAILABILITY_DTO_LIST_JSON_PATH = "src/test/resources/payload/availibility/dto/availability-dto-list.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Availability availability() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(AVAILABILITY_JSON_PATH),
                Availability.class);
    }

    public static Availability updatedAvailability() {
        return Availability.builder()
                .professionalEmail("a@gmail.com")
                .availableDays(new ArrayList<>(List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)))
                .startTime(LocalTime.of(18, 00))
                .endTime(LocalTime.of(23, 59))
                .appointmentDurationMinutes(45)
                .build();
    }

    public static List<Availability> listAvailability() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(AVAILABILITY_LIST_JSON_PATH),
                new TypeReference<List<Availability>>() {
                });
    }

    public static AvailabilityDto availabilityDto() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(AVAILABILITY_DTO_JSON_PATH),
                AvailabilityDto.class);
    }

    public static AvailabilityDto updatedAvailabilityDto() {
        return new AvailabilityDto(
                "a@gmail.com",
                new ArrayList<DayOfWeek>(List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
                LocalTime.of(18, 00),
                LocalTime.of(23, 59),
                45);
    }

    public static List<AvailabilityDto> listAvailabilityDto() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(AVAILABILITY_DTO_LIST_JSON_PATH),
                new TypeReference<List<AvailabilityDto>>() {
                });
    }

}
