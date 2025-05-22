package com.augusto.appointment_system.unit.repository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.model.Client;
import com.augusto.appointment_system.repository.ClientRepository;

@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;

    private Client client;
    private List<Client> clientList = new ArrayList<>();

    @BeforeEach
    void setup() {
        client = Client.builder()
                .name("John Doe")
                .email("a@email.com")
                .phone("3499999999")
                .build();
        clientList.add(client);
        clientList.add(Client.builder()
                .name("Jane Doe")
                .email("b@email.com")
                .phone("3499999999")
                .build());
        clientList.add(Client.builder()
                .name("Max Smith")
                .email("c@email.com")
                .phone("3499999999")
                .build());
        clientList.add(Client.builder()
                .name("Carlo San")
                .email("d@email.com")
                .phone("3499999999")
                .build());
    }

    @Test
    public void givenClient_whenSave_thenReturnSavedEmoployee() {
        // given - precodition or setup
        var savedClient = clientRepository.save(client);

        // then - verify result
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getId()).isGreaterThan(0);
        assertThat(savedClient.getName()).isEqualTo(client.getName());
        assertThat(savedClient.getEmail()).isEqualTo(client.getEmail());
        assertThat(savedClient.getPhone()).isEqualTo(client.getPhone());
    }

    @Test
    public void givenEmail_whenVerifyIfExist_thenReturnBool() {
        // given - precodition or setup
        var clientEmailExist = clientRepository.existsByEmail(client.getEmail());
        // then - verify result
        assertThat(clientEmailExist).isFalse();
    }

    @Test
    public void givenListClient_whenSelectAll_thenReturnListOfClients() {
        // given - precodition or setup
        clientRepository.saveAll(clientList);
        // when - action or the behavior that we are goint to test
        var result = clientRepository.findAll();
        // then - verify the result
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(clientList.size());
    }

    @Test
    public void givenId_whenFindById_thenRetunrnClient() {
        // given - precodition or setup
        var savedClient = clientRepository.save(client);
        // when - action or the behavior that we are goint to test
        var client = clientRepository.findById(savedClient.getId());
        // then - verify the result
        assertThat(client).isNotEmpty();
        assertThat(client.get()).isEqualTo(savedClient);
    }

    @Test
    public void givenClient_whenUpdateClient_thenReturnClient() {
        // given - precodition or setup
        clientRepository.save(client);
        var updatedClient = Client.builder()
                .name("Augusto Marley")
                .email("augusto@email.com")
                .phone("34988777625")
                .build();
        // when - action or the behavior that we are goint to test
        client = clientRepository.findById(client.getId()).get();
        client.setEmail(updatedClient.getEmail());
        client.setName(updatedClient.getName());
        client.setPhone(updatedClient.getPhone());
        client = clientRepository.save(client);
        // then - verify the result
        assertThat(client.getId()).isEqualTo(client.getId());
        assertThat(client.getName()).isEqualTo(updatedClient.getName());
        assertThat(client.getEmail()).isEqualTo(updatedClient.getEmail());
        assertThat(client.getPhone()).isEqualTo(updatedClient.getPhone());
    }

    @Test
    public void givenId_whenDeleteClient_thenVerifyResult() {
        // given - precodition or setup
        clientRepository.save(client);
        // when - action or the behavior that we are goint to test
        clientRepository.deleteById(client.getId());
        // then - verifyu the resul
        assertThat(clientRepository.findById(client.getId())).isEmpty();
    }

    @Test
    public void given_when_then() {
        // given - precodition or setup

        // when - action or the behavior that we are goint to test

        // then - verifyu the resul
    }
}
