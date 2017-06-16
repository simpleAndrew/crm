package org.audibene.task.crm.domain;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
import org.audibene.task.crm.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {


    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Optional<Appointment> getNearestAppointment(Long clientId, LocalDateTime since) {
        Date fromDate = Appointment.toDate(since);
        List<Appointment> nextNearestAppointments = appointmentRepository.getNextAppointments(clientId, fromDate);

        if (nextNearestAppointments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(nextNearestAppointments.get(0));
    }

    public Optional<Appointment> getLastVisitedAppointment(Long clientId, LocalDateTime upToDate) {
        Date toDate = Appointment.toDate(upToDate);
        List<Appointment> previousAppointments = appointmentRepository.getPreviousAppointments(clientId, toDate);

        if (previousAppointments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(previousAppointments.get(0));
    }

    public Appointment createAppointment(Long clientId, Appointment appointment) {
        Client clientToCreateAppointment = clientRepository.findOne(clientId);

        if(clientToCreateAppointment == null) {
            throw new IllegalStateException("Client with id " + clientId + "does not exist");
        }

        appointment.setClient(clientToCreateAppointment);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsForClient(Long clientId) {
        return appointmentRepository.getAllClientAppointments(clientId);
    }

    public List<Appointment> getNextWeekAppointments(LocalDateTime since) {
        LocalDateTime fromDate = since.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime dateWeekAfter = fromDate.plusWeeks(1L);
        return appointmentRepository.findForPeriod(Appointment.toDate(fromDate), Appointment.toDate(dateWeekAfter));
    }


    public Optional<Appointment> rateAppointment(Long appointmentId, String rating) {
        Appointment appointmentToRate = appointmentRepository.findOne(appointmentId);

        if (appointmentToRate == null) {
            return Optional.empty();
        }

        appointmentToRate.setRating(rating);
        return Optional.of(appointmentRepository.save(appointmentToRate));
    }
}
