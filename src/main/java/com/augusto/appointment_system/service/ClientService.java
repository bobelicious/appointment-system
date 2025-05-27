package com.augusto.appointment_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.appointment_system.dto.ClientDto;
import com.augusto.appointment_system.exception.ClientException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.mapper.ClientMapper;

import static com.augusto.appointment_system.mapper.ClientMapper.*;

import java.util.List;

import com.augusto.appointment_system.repository.ClientRepository;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ClientDto saveClient(ClientDto clientDto) {
        validEmail(clientDto.getEmail());
        return mapToClientDto(clientRepository.save(mapToClient(clientDto)));
    }

    public ClientDto findClientById(Long id) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("client", "id", id));
        return mapToClientDto(client);
    }

    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::mapToClientDto).toList();
    }

    public ClientDto updateClient(ClientDto updatedClientDto, Long id) {
        validEmail(updatedClientDto.getEmail());
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        client.setName(updatedClientDto.getName());
        client.setEmail(updatedClientDto.getEmail());
        client.setPhone(updatedClientDto.getPhone());
        client = clientRepository.save(client);
        return mapToClientDto(client);
    }

    public void deleteClientById(Long id) {
        clientRepository.findById(id)
                .ifPresentOrElse(clientRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("client", "id", id);
                        });
    }

    private void validEmail(String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "email already exist");
        }
    }
}
