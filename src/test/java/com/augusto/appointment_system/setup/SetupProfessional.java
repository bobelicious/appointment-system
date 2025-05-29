package com.augusto.appointment_system.setup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.augusto.appointment_system.dto.ProfessionalDto;
import com.augusto.appointment_system.model.Professional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SetupProfessional {
    private static final String PROFESSIONAL_JSON_PATH = "src/test/resources/payload/professional/entity/professional.json";
    private static final String PROFESSIONAL_LIST_JSON_PATH = "src/test/resources/payload/professional/entity/professional-list.json";
    private static final String PROFESSIONAL_DTO_JSON_PATH = "src/test/resources/payload/professional/dto/professional-dto.json";
    private static final String PROFESSIONAL_DTO_LIST_JSON_PATH = "src/test/resources/payload/professional/dto/professional-list-dto.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Professional professional() throws IOException {
        return objectMapper.readValue(
                new File(PROFESSIONAL_JSON_PATH),
                Professional.class);
    }

    public static List<Professional> professionalList() throws IOException {
        return objectMapper.readValue(
                new File(PROFESSIONAL_LIST_JSON_PATH),
                new TypeReference<List<Professional>>() {
                });
    }

    public static ProfessionalDto professionalDto() throws IOException {
        return objectMapper.readValue(
                new File(PROFESSIONAL_DTO_JSON_PATH),
                ProfessionalDto.class);
    }

    public static List<ProfessionalDto> professionalDtoList() throws IOException {
        return objectMapper.readValue(
                new File(PROFESSIONAL_DTO_LIST_JSON_PATH),
                new TypeReference<List<ProfessionalDto>>() {
                });
    }

    public static ProfessionalDto updatedProfessionalDto() {
        return new ProfessionalDto("Anthony Jarad Higgins", "jarad@gmail.com", "Singer");
    }

    public static Professional updatedProfessional() {
        return Professional.builder()
                .name("Anthony Jarad Higgins")
                .email("jarad@gmail.com")
                .specialty("Singer")
                .build();
    }
}
