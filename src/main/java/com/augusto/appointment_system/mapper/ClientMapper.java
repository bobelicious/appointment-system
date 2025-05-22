package com.augusto.appointment_system.mapper;

import com.augusto.appointment_system.dto.ClientDto;
import com.augusto.appointment_system.model.Client;

public class ClientMapper {
    public static ClientDto mapToClientDto(Client client) {
        return new ClientDto(client.getName(), client.getEmail(), client.getPhone());
    }

    public static Client mapToClient(ClientDto clientDto) {
        return Client.builder()
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .build();
    }
}
