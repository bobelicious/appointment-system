package com.augusto.appointment_system.unit_tests.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.model.Professional;
import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DataJpaTest
public class ProfessionalRepositoryTest {
    @Autowired
    ProfessionalRepository professionalRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String PROFESSIONAL_JSON_PATH = "src/test/resources/payload/professional/entity/professional.json";
    private static final String PROFESSIONAL_LIST_JSON_PATH = "src/test/resources/payload/professional/entity/professional-list.json";

    private Professional professional;
    private List<Professional> professionalList = new ArrayList<>();

    @BeforeEach
    void setup() {
        try {
            professional = objectMapper.readValue(
                    new File(PROFESSIONAL_JSON_PATH),
                    Professional.class);
        } catch (IOException e) {
            e.printStackTrace();

        }

        try {
            professionalList = objectMapper.readValue(
                    new File(PROFESSIONAL_LIST_JSON_PATH),
                    new TypeReference<List<Professional>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenProfessional_whenSave_thenReturnSavedProfesssional() {
        // given - precodition or setup
        var savedProfessional = professionalRepository.save(professional);

        // then - verify result
        assertThat(savedProfessional).isNotNull();
        assertThat(savedProfessional.getId()).isGreaterThan(0);
        assertThat(savedProfessional.getName()).isEqualTo(professional.getName());
        assertThat(savedProfessional.getEmail()).isEqualTo(professional.getEmail());
        assertThat(savedProfessional.getSpecialty()).isEqualTo(professional.getSpecialty());
    }

    @Test
    public void givenEmail_whenVerifyIfExist_thenReturnBool() {
        // given - precodition or setup
        var professionalEmailexists = professionalRepository.existsByEmail(professional.getEmail());
        // then - verify result
        assertThat(professionalEmailexists).isFalse();
    }

    @Test
    public void givenListProfessional_whenSelectAll_thenReturnListOfProfessionals() {
        // given - precodition or setup
        professionalRepository.saveAll(professionalList);
        // when - action or the behavior that we are goint to test
        var result = professionalRepository.findAll();
        // then - verify the result
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(professionalList.size());
    }

    @Test
    public void givenId_whenFindById_thenRetunrnProfessional() {
        // given - precodition or setup
        var savedProfessional = professionalRepository.save(professional);
        // when - action or the behavior that we are goint to test
        var professional = professionalRepository.findById(savedProfessional.getId());
        // then - verify the result
        assertThat(professional).isNotEmpty();
        assertThat(professional.get()).isEqualTo(savedProfessional);
    }

    @Test
    public void givenProfessional_whenUpdateProfessional_thenReturnProfessional() {
        // given - precodition or setup
        professionalRepository.save(professional);
        var updatedProfessional = Professional.builder()
                .name("Augusto Marley")
                .email("augusto@email.com")
                .specialty("Software Engineer")
                .build();
        // when - action or the behavior that we are goint to test
        professional = professionalRepository.findById(professional.getId()).get();
        professional.setEmail(updatedProfessional.getEmail());
        professional.setName(updatedProfessional.getName());
        professional.setSpecialty(updatedProfessional.getSpecialty());
        professional = professionalRepository.save(professional);
        // then - verify the result
        assertThat(professional.getId()).isEqualTo(professional.getId());
        assertThat(professional.getName()).isEqualTo(updatedProfessional.getName());
        assertThat(professional.getEmail()).isEqualTo(updatedProfessional.getEmail());
        assertThat(professional.getSpecialty()).isEqualTo(updatedProfessional.getSpecialty());
    }

    @Test
    public void givenId_whenDeleteProfessional_thenVerifyResult() {
        // given - precodition or setup
        professionalRepository.save(professional);
        // when - action or the behavior that we are goint to test
        professionalRepository.deleteById(professional.getId());
        // then - verify the result
        assertThat(professionalRepository.findById(professional.getId())).isEmpty();
    }

}
