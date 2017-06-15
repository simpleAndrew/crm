package org.audibene.task.crm.domain;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
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

    public List<Appointment> getAppointmentsForClient(Long clientId) {
        return appointmentRepository.getAllClientAppointments(clientId);
    }

    public List<Appointment> getNextWeekAppointments(LocalDateTime since) {
        LocalDateTime fromDate = since.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime dateWeekAfter = fromDate.plusWeeks(1L);
        return appointmentRepository.findForPeriod(Appointment.toDate(fromDate), Appointment.toDate(dateWeekAfter));
    }


    public Appointment rateAppointment(Long appointmentId, String rating) {
        Appointment one = appointmentRepository.findOne(appointmentId);
        one.setRating(rating);
        return appointmentRepository.save(one);
    }
}
