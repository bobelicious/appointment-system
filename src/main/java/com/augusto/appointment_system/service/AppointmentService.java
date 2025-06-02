package com.augusto.appointment_system.service;

import static com.augusto.appointment_system.mapper.AppointmentMapper.mapToAppointment;
import static com.augusto.appointment_system.mapper.AppointmentMapper.mapToappointmentDto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.augusto.appointment_system.config.MailConfig;
import com.augusto.appointment_system.dto.AppointmentDto;
import com.augusto.appointment_system.exception.AppointmentException;
import com.augusto.appointment_system.exception.ResourceNotFoundException;
import com.augusto.appointment_system.model.AppointmentStatus;
import com.augusto.appointment_system.model.Client;
import com.augusto.appointment_system.model.Professional;
import com.augusto.appointment_system.repository.AppointmentRepository;
import com.augusto.appointment_system.repository.ClientRepository;
import com.augusto.appointment_system.repository.ProfessionalRepository;

@Service
public class AppointmentService {
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
        validateAppointmentDateTime(appointmentDto.startTime(), appointmentDto.endTime());
        var client = validAteAndFindAppointmentClient(appointmentDto.clientEmail());
        var professional = validAteAndFindAppointmentProfessional(appointmentDto.professionalEmail());
        var appointment = mapToAppointment(appointmentDto, client, professional);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment = appointmentRepository.save(appointment);
        sendEmail(appointmentDto.clientEmail(),
                "Confirmacao de agendamento", generateConfirmLink(appointment.getId()));
        return mapToappointmentDto(appointment);
    }

    public String confirmAppointment(Long id) {
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment confirmation", "id", id));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
        return "Status confirmed successful";
    }

    private Client validAteAndFindAppointmentClient(String clientEmail) {
        return clientRepository.findClientByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));
    }

    private Professional validAteAndFindAppointmentProfessional(String professionalEmail) {

        return professionalRepository.findProfessionalByEmail(professionalEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Professional", "email", professionalEmail));
    }

    private void validateAppointmentDateTime(LocalDateTime startTime, LocalDateTime endTime) {
        var timeNow = LocalDateTime.now();
        if (timeNow.isAfter(startTime)) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Start time is in the past");
        }

        if (startTime.isAfter(endTime)) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "End time is before than startTime");
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

}
