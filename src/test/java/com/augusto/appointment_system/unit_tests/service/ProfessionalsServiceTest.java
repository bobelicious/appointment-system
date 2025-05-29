package com.augusto.appointment_system.unit_tests.service;

import static com.augusto.appointment_system.mapper.ProfessionalMapper.mapToProfessionalDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.augusto.appointment_system.dto.ProfessionalDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.model.Professional;
import com.augusto.appointment_system.repository.ProfessionalRepository;
import com.augusto.appointment_system.service.ProfessionalService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ProfessionalsServiceTest {
    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private ProfessionalService professionalService;

    private static final String PROFESSIONAL_JSON_PATH = "src/test/resources/payload/professional/entity/professional.json";
    private static final String PROFESSIONAL_LIST_JSON_PATH = "src/test/resources/payload/professional/entity/professional-list.json";
    private static final String PROFESSIONAL_DTO_JSON_PATH = "src/test/resources/payload/professional/dto/professional-dto.json";
    private static final String PROFESSIONAL_DTO_LIST_JSON_PATH = "src/test/resources/payload/professional/dto/professional-list-dto.json";

    private Professional professional;
    private List<Professional> professionalList = new ArrayList<>();
    private ProfessionalDto professionalDto;
    private List<ProfessionalDto> professionalDtoList = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        try {
            professionalDto = objectMapper.readValue(
                    new File(PROFESSIONAL_DTO_JSON_PATH),
                    ProfessionalDto.class);

            professional = objectMapper.readValue(
                    new File(PROFESSIONAL_JSON_PATH),
                    Professional.class);

            professionalDtoList = objectMapper.readValue(
                    new File(PROFESSIONAL_DTO_LIST_JSON_PATH),
                    new TypeReference<List<ProfessionalDto>>() {
                    });

            professionalList = objectMapper.readValue(
                    new File(PROFESSIONAL_LIST_JSON_PATH),
                    new TypeReference<List<Professional>>() {
                    });

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Test
    void givenProfessionalDto_whenSave_thenReturnProfessionalDto() {
        // given - precondition or setup
        given(professionalRepository.existsByEmail(professionalDto.email())).willReturn(false);
        given(professionalRepository.save(any(Professional.class))).willReturn(professional);

        // when - action or the behavior that we are going to test
        var savedProfessional = professionalService.saveProfessional(professionalDto);

        // then - verify the result
        assertThat(savedProfessional).isNotNull();
        assertThat(savedProfessional.email()).isEqualTo(professionalDto.email());
        assertThat(savedProfessional.name()).isEqualTo(professionalDto.name());
        assertThat(savedProfessional.specialty()).isEqualTo(professionalDto.specialty());
    }

    @Test
    void givenProfessionalDto_whenSave_thenThrowsException() {
        // given - precodition or setup
        given(professionalRepository.existsByEmail(professional.getEmail())).willReturn(true);
        // when - action or the behavior that we are goint to test
        assertThrows(AppointmentException.class, () -> {
            professionalService.saveProfessional(professionalDto);
        });
        // then - verify the result
        verify(professionalRepository, never()).save(any(Professional.class));
    }

    @Test
    void givenId_whenFindProfessionalById_thenReturnProfessionalDto() {
        // given - precondition or setup
        given(professionalRepository.findById(1L)).willReturn(Optional.of(professional));

        // when - action or the behavior that we are going to test
        var result = professionalService.findById(1L);

        // then - verify the result
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(professionalDto.name());
        assertThat(result.email()).isEqualTo(professionalDto.email());
        assertThat(result.specialty()).isEqualTo(professionalDto.specialty());
    }

    @Test
    void givenId_whenFind_thenThrowsException() {
        // given - precodition or setup
        given(professionalRepository.findById(1L)).willReturn(Optional.empty());
        // when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            professionalService.findById(1L);
        });
    }

    @Test
    void givenProfessionalList_whenFindAll_thenReturnProfessionalDtoList() {
        // given - precodition or setup
        given(professionalRepository.findAll()).willReturn(professionalList);

        // then - action or behaviour that we are going to test
        var result = professionalService.findAll();

        // when - verify result
        assertThat(result).isEqualTo(professionalDtoList);
    }

    @Test
    void givenUpdatedProfessional_whenUpdateProfessional_thenRetunrProfessional() {
        // given - precodition or setup
        var updatedProfessional = Professional.builder()
                .name("Anthony Jarad Higgins")
                .email("juice@email.com")
                .specialty("Singer")
                .build();
        given(professionalRepository.existsByEmail(updatedProfessional.getEmail())).willReturn(false);
        given(professionalRepository.findById(1L)).willReturn(Optional.of(professional));
        given(professionalRepository.save(any(Professional.class))).willReturn(updatedProfessional);

        // when - action or behaviorthat we are going to test
        var result = professionalService.updateProfessional(mapToProfessionalDto(updatedProfessional), 1L);

        // then - verify the result
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(updatedProfessional.getName());
        assertThat(result.email()).isEqualTo(updatedProfessional.getEmail());
        assertThat(result.specialty()).isEqualTo(updatedProfessional.getSpecialty());
    }

    @Test
    void givenUpdatedProfessional_whenUpdateProfessional_thenThrowsException() {
        // given - precodition or setup
        var updatedProfessional = Professional.builder()
                .name("Anthony Jarad Higgins")
                .email("juice@email.com")
                .specialty("Singer")
                .build();
        given(professionalRepository.existsByEmail(updatedProfessional.getEmail())).willReturn(true);

        // when - action or behaviorthat we are going to test
        assertThrows(AppointmentException.class, () -> {
            professionalService.updateProfessional(mapToProfessionalDto(updatedProfessional), 1L);
        });

        // then - verify the result
        verify(professionalRepository, never()).save(any(Professional.class));
    }

    @Test
    void givenUpdatedProfessional_whenUpdateProfessional_thenThrowsResourceNotFound() {
        // given - precodition or setup
        var updatedProfessional = Professional.builder()
                .name("Anthony Jarad Higgins")
                .email("juice@email.com")
                .specialty("Singer")
                .build();
        given(professionalRepository.findById(1L)).willReturn(Optional.empty());

        // when - action or behaviorthat we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            professionalService.updateProfessional(mapToProfessionalDto(updatedProfessional), 1L);
        });

        // then - verify the result
        verify(professionalRepository, never()).save(any(Professional.class));
    }

    @Test
    public void givenId_whenDeleteProfessional_thenVerify() {
        // given - precodition or setup
        given(professionalRepository.findById(1L)).willReturn(Optional.of(professional));
        willDoNothing().given(professionalRepository).delete(professional);
        // when - action or the behavior that we are goint to test
        professionalService.deleteProfessional(1L);
        // then - verify the result
        then(professionalRepository).should().delete(professional);
    }

    @Test
    public void givenId_whenDeleteProfessional_thenThrowsResourceNotFoundException() {
        // given - precodition or setup
        given(professionalRepository.findById(1L)).willReturn(Optional.empty());
        // when - action or the behavior that we are goint to test
        assertThrows(ResourceNotFoundException.class, () -> {
            professionalService.deleteProfessional(1L);
        });
        // then - verify the result
        verify(professionalRepository, never()).delete(any(Professional.class));
    }

}
