package com.augusto.appointment_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.appointment_system.dto.ClientDto;
import com.augusto.appointment_system.exception.ClientException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;

import static com.augusto.appointment_system.mapper.ClientMapper.*;
import com.augusto.appointment_system.repository.ClientRepository;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ClientDto saveClient(ClientDto clientDto) {
        if (clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "email already exist");
        }
        return mapToClientDto(clientRepository.save(mapToClient(clientDto)));
    }

    public ClientDto findClientById(Long id) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("client", "id", id));
        return mapToClientDto(client);
    }
}
