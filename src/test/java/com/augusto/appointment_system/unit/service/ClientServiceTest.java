package com.augusto.appointment_system.unit.service;

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
import com.augusto.appointment_system.model.Client;

import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.service.ClientService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientDto clientDto;
    private Client client;
    private List<ClientDto> clientList = new ArrayList<>();

    @BeforeEach
    void setup() {
        clientDto = new ClientDto("John Doe", "a@email.com", "3499999999");
        clientList.add(clientDto);
        clientList.add(new ClientDto("Jane Doe", "b@email.com", "3499999999"));
        clientList.add(new ClientDto("Max Smith", "c@email.com", "3499999999"));
        clientList.add(new ClientDto("Carlo San", "d@email.com", "3499999999"));

        client = new Client(1L,"John Doe", "a@email.com","1234567890");
    }

    @Test
    void givenClientDto_whenSave_thenReturnClientDto() {
        // given - precondition or setup
        given(clientRepository.existsByEmail(clientDto.getEmail())).willReturn(false);
        given(clientRepository.save(any(Client.class))).willReturn(client);

        // when - action or the behavior that we are going to test
        ClientDto savedClient = clientService.saveClient(clientDto);

        // then - verify the result
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getEmail()).isEqualTo(clientDto.getEmail());
    }

    @Test
    public void givenClientDto_whenSave_thenThrowsException() {
        // given - precodition or setup

        // when - action or the behavior that we are goint to test

        // then - verify the result
    }

    // @Test
    // public void given_when_then() {
    // // given - precodition or setup

    // // when - action or the behavior that we are goint to test

    // // then - verify the result
    // }
    // @Test
    // public void given_when_then() {
    // // given - precodition or setup

    // // when - action or the behavior that we are goint to test

    // // then - verify the result
    // }
    // @Test
    // public void given_when_then() {
    // // given - precodition or setup

    // // when - action or the behavior that we are goint to test

    // // then - verify the result
    // }
}
