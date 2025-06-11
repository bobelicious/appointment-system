package com.augusto.appointment_system.setup;

import java.io.File;
import java.io.IOException;

import com.augusto.appointment_system.dto.AvailabilityDto;
import com.augusto.appointment_system.model.Availability;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SetupAvailability {
    private static String AVAILABILITY_JSON_PATH = "src/test/resources/payload/availibility/entity/availibility.json";
    private static String AVAILABILITY_DTO_JSON_PATH = "src/test/resources/payload/availibility/dto/availibility-dto.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Availability availability() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(AVAILABILITY_JSON_PATH),
                Availability.class);
    }
    public static AvailabilityDto availabilityDto() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(
                new File(AVAILABILITY_DTO_JSON_PATH),
                AvailabilityDto.class);
    }

}
