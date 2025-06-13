package com.augusto.appointment_system.service;

import static com.augusto.appointment_system.mapper.AppointmentMapper.mapToAppointment;
import static com.augusto.appointment_system.mapper.AppointmentMapper.mapToappointmentDto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.appointment_system.config.MailConfig;
import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.mapper.AppointmentMapper;
import com.augusto.appointment_system.model.Appointment;
import com.augusto.appointment_system.model.AppointmentStatus;
import com.augusto.appointment_system.model.Availability;
import com.augusto.appointment_system.model.Client;
import com.augusto.appointment_system.model.Professional;
import com.augusto.appointment_system.repository.AppointmentRepository;
import com.augusto.appointment_system.repository.AvailabilityRepository;
import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;

@Service
public class AppointmentService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private Environment environment;

    public AppointmentDto createAppointment(AppointmentDto appointmentDto) throws UnknownHostException {

        validateAppointmentDateTime(appointmentDto.startTime());

        var professional = getValidatedProfessional(appointmentDto.professionalEmail());
        var availability = checkAvailability(professional.getEmail(), appointmentDto.startTime());
        var client = getValidatedClient(appointmentDto.clientEmail());

        var appointment = createScheduledAppointment(appointmentDto, client, professional, availability);

        sendEmail(appointmentDto.clientEmail(),
                "Confirmacao de agendamento", generateConfirmLink(appointment.getId()));
        return mapToappointmentDto(appointment);
    }

    public String confirmAppointment(Long id) {
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment confirmation", "id", id));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
        var message = String.format("Agendamento confirmado com sucesso, id: %s", appointment.getId());
        sendEmail(appointment.getProfessional().getEmail(), "Confirmacao de agendamento", message);
        return "Status confirmed successful";
    }

    public List<AppointmentDto> ListClientScheduledAppointments(String clientEmail) {
        getValidatedClient(clientEmail);
        var scheduledAppointments = appointmentRepository.findAllByClientEmail(clientEmail);
        return scheduledAppointments.stream().map(AppointmentMapper::mapToappointmentDto).toList();
    }

    private Appointment createScheduledAppointment(AppointmentDto dto, Client client, Professional professional,
            Availability availability) {
        var endTime = dto.startTime().plusMinutes(availability.getAppointmentDurationMinutes());
        var appointment = mapToAppointment(dto, client, professional);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    private Client getValidatedClient(String clientEmail) {
        return clientRepository.findClientByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));
    }

    private Professional getValidatedProfessional(String professionalEmail) {

        return professionalRepository.findProfessionalByEmail(professionalEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Professional", "email", professionalEmail));
    }

    private void validateAppointmentDateTime(LocalDateTime startTime) {
        var timeNow = LocalDateTime.now();
        if (timeNow.isAfter(startTime)) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Start time is in the past");
        }
    }

    private void sendEmail(String to, String subject, String text) {
        mailConfig.sendEmail(to, subject, text);
    }

    private String generateConfirmLink(Long id) throws UnknownHostException {
        var host = InetAddress.getLocalHost().getHostAddress();
        var port = environment.getProperty("local.server.port");
        return String.format(
                "Agendamento feito com sucesso, confirme no link a seguir http://%s:%s/api/v1/appointment/confirm/%s",
                host, port, id);
    }

    private Availability checkAvailability(String professionalEmail, LocalDateTime starTime) {
        var availability = availabilityRepository.findAvailabilityByProfessionalEmail(professionalEmail).orElseThrow(
                () -> new ResourceNotFoundException("Availability", "professionalEmail", professionalEmail));

        var contains = availability.getAvailableDays().stream().anyMatch((day) -> starTime.getDayOfWeek().equals(day));
        if (!contains) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Unavailable day");
        }
        if (appointmentRepository.existsByStartTimeAndProfessionalEmail(starTime, professionalEmail)
                || availability.getStartTime().isAfter(starTime.toLocalTime())
                || availability.getEndTime().isBefore(starTime.toLocalTime())) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Unavailable time");
        }
        return availability;
    }

}
