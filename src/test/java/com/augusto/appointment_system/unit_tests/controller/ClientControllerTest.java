package com.augusto.appointment_system.unit_tests.controller;

import static com.augusto.appointment_system.setup.SetupClient.clientDto;
import static com.augusto.appointment_system.setup.SetupClient.clientDtoList;
import static com.augusto.appointment_system.setup.SetupClient.updatedClientDto;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.augusto.appointment_system.controller.AvailabilityController;
import com.augusto.appointment_system.dto.ClientDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.service.AppointmentService;
import com.augusto.appointment_system.service.ClientService;
import com.augusto.appointment_system.service.ProfessionalService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class ClientControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ClientService clientService;
	@MockitoBean
	private ProfessionalService professionalService;
	@MockitoBean
	private AppointmentService appointmentService;
	@MockitoBean
	private AvailabilityController availabilityController;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void givenClientDto_whenSaveClient_thenReturnClientDto() throws Exception {
		// given - precodition or setup
		given(clientService.saveClient(any(ClientDto.class)))
				.willAnswer(invocation -> invocation.getArgument(0));

		// when - action or behaviour that we are goint test
		var result = mockMvc.perform(post("/api/v1/client/new")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDto())));

		// then - verify
		result.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name",
						is(clientDto().getName())))
				.andExpect(jsonPath("$.email",
						is(clientDto().getEmail())));
	}

	@Test
	void givenClientDto_whenSaveClient_thenThrowsAppointmentException() throws Exception {
		// given - precodition or setup
		given(clientService.saveClient(any(ClientDto.class)))
				.willThrow(AppointmentException.class);

		// when - action or behaviour that we are goint test
		var result = mockMvc.perform(post("/api/v1/client/new")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDto())));

		// then - verify
		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenClientId_whenFindById_thenReturnClientDto() throws Exception {
		// given - precodition or setup
		given(clientService.findClientById(1L))
				.willReturn(clientDto());
		// when - action or behaviour that we are goint test
		var result = mockMvc.perform(get("/api/v1/client/{id}", 1L));

		// then - verify result
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(clientDto().getName())))
				.andExpect(jsonPath("$.email", is(clientDto().getEmail())));
	}

	@Test
	void givenClientList_whenFindAll_thenReturnListOfClientDto() throws Exception {
		// given - precodition or setup
		given(clientService.findAll())
				.willReturn(clientDtoList());
		// when - action or behaviour that we are goint to test
		var result = mockMvc.perform(get("/api/v1/client/list-all"));

		// then - verify result
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(clientDtoList().size())));
	}

	@Test
	void givenClientDto_whenUpdate_thenReturnClientDto() throws Exception {
		// given - precodition or setup
		given(clientService.updateClient(any(ClientDto.class), any(Long.class)))
				.willAnswer(invocation -> invocation.getArgument(0));
		// when - action or behaviour that we are goint to test
		var result = mockMvc.perform(
				put("/api/v1/client/update/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedClientDto())));

		// then - verify result
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(updatedClientDto().getName())))
				.andExpect(jsonPath("$.email", is(updatedClientDto().getEmail())))
				.andExpect(jsonPath("$.phone", is(updatedClientDto().getPhone())));

	}

	@Test
	void givenClientDto_whenUpdate_thenThrowsRetunrNotFoundException() throws Exception {
		// given - precodition or setup
		given(clientService.updateClient(any(ClientDto.class), any(Long.class)))
				.willThrow(ResourceNotFoundException.class);
		// when - action or behaviour that we are goint to test
		var result = mockMvc.perform(
				put("/api/v1/client/update/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedClientDto())));

		// then - verify result
		result.andExpect(status().isNotFound());
	}

	@Test
	void givenClientDto_whenUpdate_thenThrowsAppointmentException() throws Exception {
		// given - precodition or setup
		given(clientService.updateClient(any(ClientDto.class), any(Long.class)))
				.willThrow(AppointmentException.class);
		// when - action or behaviour that we are goint to test
		var result = mockMvc.perform(
				put("/api/v1/client/update/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedClientDto())));

		// then - verify result
		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenClientId_whenDeleteClient_thenVerify() throws Exception {
		// given - precodition or setup
		willDoNothing().given(clientService).deleteClientById(1L);

		// when - action or the behaviour that we are goin test
		var result = mockMvc.perform(delete("/api/v1/client/delete/{id}", 1L));

		// then - verify
		result.andExpect(status().isNoContent());
	}

	@Test
	void givenClientId_whenDeleteClient_thenThrowsResourceNotFoundException() throws Exception {
		// given - precodition or setup
		willThrow(ResourceNotFoundException.class)
				.given(clientService)
				.deleteClientById(1L);
		// when - action or the behaviour that we are goin test
		var result = mockMvc.perform(delete("/api/v1/client/delete/{id}", 1L));

		// then - verify
		result.andExpect(status().isNotFound());
	}
}
