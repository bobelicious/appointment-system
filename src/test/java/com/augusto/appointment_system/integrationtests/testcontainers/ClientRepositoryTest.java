package com.augusto.appointment_system.integrationtests.testcontainers;

import static com.augusto.appointment_system.setup.SetupClient.client;
import static com.augusto.appointment_system.setup.SetupClient.clientList;
import static com.augusto.appointment_system.setup.SetupClient.updatedClient;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.augusto.appointment_system.repository.ClientRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClientRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    ClientRepository clientRepository;

    @Test
    public void givenClient_whenSave_thenReturnSavedEmoployee() throws IOException {
        // given - precodition or setup
        var savedClient = clientRepository.save(client());

        // then - verify result
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getId()).isGreaterThan(0);
        assertThat(savedClient.getName()).isEqualTo(client().getName());
        assertThat(savedClient.getEmail()).isEqualTo(client().getEmail());
        assertThat(savedClient.getPhone()).isEqualTo(client().getPhone());
    }

    @Test
    public void givenEmail_whenVerifyIfExist_thenReturnBool() throws IOException {
        // given - precodition or setup
        var clientEmailExist = clientRepository.existsByEmail(client().getEmail());
        // then - verify result
        assertThat(clientEmailExist).isFalse();
    }

    @Test
    public void givenListClient_whenSelectAll_thenReturnListOfClients() throws Exception {
        // given - precodition or setup
        clientRepository.saveAll(clientList());
        // when - action or the behavior that we are goint to test
        var result = clientRepository.findAll();
        // then - verify the result
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(clientList().size());
    }

    @Test
    public void givenId_whenFindById_thenRetunrnClient() throws IOException {
        // given - precodition or setup
        var savedClient = clientRepository.save(client());
        // when - action or the behavior that we are goint to test
        var client = clientRepository.findById(savedClient.getId());
        // then - verify the result
        assertThat(client).isNotEmpty();
        assertThat(client.get()).isEqualTo(savedClient);
    }

    @Test
    public void givenClient_whenUpdateClient_thenReturnClient() throws IOException {
        // given - precodition or setup
        var client = clientRepository.save(client());
        // when - action or the behavior that we are goint to test
        client = clientRepository.findById(client.getId()).get();
        client.setEmail(updatedClient().getEmail());
        client.setName(updatedClient().getName());
        client.setPhone(updatedClient().getPhone());
        client = clientRepository.save(client);
        // then - verify the result
        assertThat(client.getId()).isEqualTo(client.getId());
        assertThat(client.getName()).isEqualTo(updatedClient().getName());
        assertThat(client.getEmail()).isEqualTo(updatedClient().getEmail());
        assertThat(client.getPhone()).isEqualTo(updatedClient().getPhone());
    }

    @Test
    public void givenId_whenDeleteClient_thenVerifyResult() throws IOException {
        // given - precodition or setup
        var client = clientRepository.save(client());
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
