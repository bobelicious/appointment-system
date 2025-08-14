package com.augusto.appointment_system.unit_tests.service;

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

import com.augusto.appointment_system.dto.ClientDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.model.Client;
import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.service.ClientService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private static final String CLIENT_JSON_PATH = "src/test/resources/payload/client/entity/client.json";
    private static final String CLIENT_LIST_JSON_PATH = "src/test/resources/payload/client/entity/client-list.json";
    private static final String CLIENT_DTO_JSON_PATH = "src/test/resources/payload/client/dto/client-dto.json";
    private static final String CLIENT_DTO_LIST_JSON_PATH = "src/test/resources/payload/client/dto/client-list-dto.json";

    ObjectMapper objectMapper = new ObjectMapper();

    private ClientDto clientDto;
    private ClientDto updatedClientDto;
    private Client client;
    private Client toUpdateClient;
    private List<Client> clientList = new ArrayList<>();
    private List<ClientDto> clientDtoList = new ArrayList<>();

    @BeforeEach
    void setup() {
        updatedClientDto = new ClientDto("Jarad Antony Higgins", "jarad@email.com", "34992177249");
        toUpdateClient = new Client(null, "Jarad Antony Higgins", "jarad@email.com", "34992177249");

        try {

            clientDto = objectMapper.readValue(
                    new File(CLIENT_DTO_JSON_PATH),
                    ClientDto.class);

            clientDtoList = objectMapper.readValue(
                    new File(CLIENT_DTO_LIST_JSON_PATH),
                    new TypeReference<List<ClientDto>>() {
                    });

            client = objectMapper.readValue(
                    new File(CLIENT_JSON_PATH),
                    Client.class);

            clientList = objectMapper.readValue(
                    new File(CLIENT_LIST_JSON_PATH),
                    new TypeReference<List<Client>>() {
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void givenClientDto_whenSave_thenReturnClientDto() {
        // given - precondition or setup
        given(clientRepository.existsByEmail(clientDto.getEmail())).willReturn(false);
        given(clientRepository.save(any(Client.class))).willReturn(client);

        // when - action or the behavior that we are going to test
        var savedClient = clientService.saveClient(clientDto);

        // then - verify the result
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getEmail()).isEqualTo(clientDto.getEmail());
    }

    @Test
    void givenClientDto_whenSave_thenThrowsException() {
        // given - precodition or setup
        given(clientRepository.existsByEmail(clientDto.getEmail())).willReturn(true);
        // when - action or the behavior that we are goint to test
        assertThrows(AppointmentException.class, () -> {
            clientService.saveClient(clientDto);
        });
        // then - verify the result
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void givenId_whenFindById_thenReturnClientDto() {
        // given - precodition or setup
        given(clientRepository.findById(1L)).willReturn(Optional.of(client));
        // when - action or the behavior that we are goint to test
        var result = clientService.findClientById(1L);
        // then - verify the result
        assertThat(result.getName()).isEqualTo(clientDto.getName());
        assertThat(result.getEmail()).isEqualTo(clientDto.getEmail());
        assertThat(result.getPhone()).isEqualTo(clientDto.getPhone());
    }

    @Test
    void givenId_whenFindById_thenThrowClientNotFound() {
        // given - precodition or setup
        given(clientRepository.findById(1L)).willReturn(Optional.ofNullable(null));
        // when - action or the behavior that we are goint to test
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.findClientById(1L);
        });
    }

    @Test
    void givenListOfClients_whenFindAll_thenReturnListOfClietsDto() {
        // given - precodition or setup
        given(clientRepository.findAll()).willReturn(clientList);
        // when - action or the behavior that we are goint to test
        var findAllClients = clientService.findAll();
        // then - verify the result
        assertThat(findAllClients.size()).isEqualTo(clientDtoList.size());
    }

    @Test
    void givenClientDto_whenUpdateClient_thenReturnUpdatedClientDto() {
        // given - precodition or setup
        given(clientRepository.existsByEmail(toUpdateClient.getEmail())).willReturn(false);
        given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));
        given(clientRepository.save(any(Client.class))).willReturn(toUpdateClient);
        // when - action or the behavior that we are goint to test
        clientDto = clientService.updateClient(updatedClientDto, toUpdateClient.getId());
        // then - verify the result
        assertThat(clientDto.getName()).isEqualTo(updatedClientDto.getName());
        assertThat(clientDto.getEmail()).isEqualTo(updatedClientDto.getEmail());
        assertThat(clientDto.getPhone()).isEqualTo(updatedClientDto.getPhone());
    }

    @Test
    void givenClientDto_whenUpdateClient_thenThrowsClientException() {
        // given - precodition or setup
        given(clientRepository.existsByEmail(updatedClientDto.getEmail())).willReturn(true);
        // when - action or the behavior that we are goint to test
        assertThrows(AppointmentException.class, () -> {
            clientService.updateClient(updatedClientDto, 1L);
        });
        // then - verify the result
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void givenClientDto_whenUpdateClient_thenThrowsResourceNotFoundException() {
        // given - precodition or setup
        given(clientRepository.existsByEmail(updatedClientDto.getEmail())).willReturn(false);
        given(clientRepository.findById(1L)).willReturn(Optional.empty());
        // when - action or the behavior that we are goint to test
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.updateClient(updatedClientDto, 1L);
        });
        // then - verify the result
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void givenId_whenDeleteClient_thenVerify() {
        // given - precodition or setup
        given(clientRepository.findById(1L)).willReturn(Optional.of(client));
        willDoNothing().given(clientRepository).delete(client);
        // when - action or the behavior that we are goint to test
        clientService.deleteClientById(1L);
        // then - verify the result
        then(clientRepository).should().delete(client);
    }

    @Test
    void givenId_whenDeleteClient_thenThrowsResourceNotFoundException() {
        // given - precodition or setup
        given(clientRepository.findById(1L)).willReturn(Optional.empty());
        // when - action or the behavior that we are goint to test
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.deleteClientById(1L);
        });
        // then - verify the result
        verify(clientRepository, never()).delete(any(Client.class));
    }

}
