package com.augusto.appointment_system.setup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.augusto.appointment_system.dto.ClientDto;
import com.augusto.appointment_system.model.Client;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SetupClient {
    private static final String CLIENT_JSON_PATH = "src/test/resources/payload/client/entity/client.json";
    private static final String CLIENT_LIST_JSON_PATH = "src/test/resources/payload/client/entity/client-list.json";
    private static final String CLIENT_DTO_JSON_PATH = "src/test/resources/payload/client/dto/client-dto.json";
    private static final String CLIENT_DTO_LIST_JSON_PATH = "src/test/resources/payload/client/dto/client-list-dto.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Client client() throws IOException {
        return objectMapper.readValue(
                new File(CLIENT_JSON_PATH),
                Client.class);
    }

    public static List<Client> clientList() throws IOException {
        return objectMapper.readValue(
                new File(CLIENT_LIST_JSON_PATH),
                new TypeReference<List<Client>>() {
                });
    }

    public static ClientDto clientDto() throws IOException {
        return objectMapper.readValue(
                new File(CLIENT_DTO_JSON_PATH),
                ClientDto.class);
    }

    public static List<ClientDto> clientDtoList() throws IOException {
        return objectMapper.readValue(
                new File(CLIENT_DTO_LIST_JSON_PATH),
                new TypeReference<List<ClientDto>>() {
                });
    }

    public static ClientDto updatedClientDto() {
        return new ClientDto("Jarad Antony Higgins", "jarad@gmail.com", "34992177249");
    }

    public static Client updatedClient() {
        return Client.builder()
                .name("Jarad Antony Higgins")
                .email("jarad@gmail.com")
                .phone("34992177249")
                .build();
    }
}
