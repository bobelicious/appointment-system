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

import com.augusto.appointment_system.model.Client;
import com.augusto.appointment_system.repository.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CLIENT_JSON_PATH = "src/test/resources/payload/client/entity/client.json";
    private static final String CLIENT_LIST_JSON_PATH = "src/test/resources/payload/client/entity/client-list.json";
    private Client client;
    private List<Client> clientList = new ArrayList<>();

    @BeforeEach
    void setup() {

        try {
            client = objectMapper.readValue(
                    new File(CLIENT_JSON_PATH),
                    Client.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clientList = objectMapper.readValue(
                    new File(CLIENT_LIST_JSON_PATH),
                    new TypeReference<List<Client>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        // then - verify the result
        assertThat(clientRepository.findById(client.getId())).isEmpty();
    }

    // @Test
    // public void given_when_then() {
    // // given - precodition or setup

    // // when - action or the behavior that we are goint to test

    // // then - verify the result
    // }
}
