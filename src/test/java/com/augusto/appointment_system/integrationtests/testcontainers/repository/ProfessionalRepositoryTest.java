package com.augusto.appointment_system.integrationtests.testcontainers.repository;

import static com.augusto.appointment_system.setup.SetupProfessional.professional;
import static com.augusto.appointment_system.setup.SetupProfessional.professionalList;
import static com.augusto.appointment_system.setup.SetupProfessional.updatedProfessional;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.integrationtests.testcontainers.AbstractIntegrationTest;
import com.augusto.appointment_system.repository.ProfessionalRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProfessionalRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    ProfessionalRepository professionalRepository;

    @Test
    public void givenProfessional_whenSave_thenReturnSavedProfesssional() throws IOException {
        // given - precodition or setup
        var savedProfessional = professionalRepository.save(professional());

        // then - verify result
        assertThat(savedProfessional).isNotNull();
        assertThat(savedProfessional.getId()).isGreaterThan(0);
        assertThat(savedProfessional.getName()).isEqualTo(professional().getName());
        assertThat(savedProfessional.getEmail()).isEqualTo(professional().getEmail());
        assertThat(savedProfessional.getSpecialty()).isEqualTo(professional().getSpecialty());
    }

    @Test
    public void givenEmail_whenVerifyIfExist_thenReturnBool() throws Exception {
        // given - precodition or setup
        var professionalEmailexists = professionalRepository.existsByEmail(professional().getEmail());
        // then - verify result
        assertThat(professionalEmailexists).isFalse();
    }

    @Test
    public void givenListProfessional_whenSelectAll_thenReturnListOfProfessionals() throws Exception {
        // given - precodition or setup
        professionalRepository.saveAll(professionalList());
        // when - action or the behavior that we are goint to test
        var result = professionalRepository.findAll();
        // then - verify the result
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(professionalList().size());
    }

    @Test
    public void givenId_whenFindById_thenRetunrnProfessional() throws IOException {
        // given - precodition or setup
        var savedProfessional = professionalRepository.save(professional());
        // when - action or the behavior that we are goint to test
        var professional = professionalRepository.findById(savedProfessional.getId());
        // then - verify the result
        assertThat(professional).isNotEmpty();
        assertThat(professional.get()).isEqualTo(savedProfessional);
    }

    @Test
    public void givenProfessional_whenUpdateProfessional_thenReturnProfessional() throws IOException {
        // given - precodition or setup
        var professional = professionalRepository.save(professional());

        // when - action or the behavior that we are goint to test
        professional = professionalRepository.findById(professional.getId()).get();
        professional.setEmail(updatedProfessional().getEmail());
        professional.setName(updatedProfessional().getName());
        professional.setSpecialty(updatedProfessional().getSpecialty());
        professional = professionalRepository.save(professional);
        // then - verify the result
        assertThat(professional.getId()).isEqualTo(professional.getId());
        assertThat(professional.getName()).isEqualTo(updatedProfessional().getName());
        assertThat(professional.getEmail()).isEqualTo(updatedProfessional().getEmail());
        assertThat(professional.getSpecialty()).isEqualTo(updatedProfessional().getSpecialty());
    }

    @Test
    public void givenId_whenDeleteProfessional_thenVerifyResult() throws IOException {
        // given - precodition or setup
        var professional = professionalRepository.save(professional());
        // when - action or the behavior that we are goint to test
        professionalRepository.deleteById(professional.getId());
        // then - verify the result
        assertThat(professionalRepository.findById(professional.getId())).isEmpty();
    }

}
