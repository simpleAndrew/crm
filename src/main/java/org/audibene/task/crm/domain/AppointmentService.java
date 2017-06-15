package org.audibene.task.crm.domain;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {


    @Autowired
    private AppointmentRepository appointmentRepository;

    public Optional<Appointment> getNearestAppointment(Long clientId) {
        Date fromDate = Appointment.toDate(LocalDateTime.now());
        List<Appointment> nextNearestAppointments = appointmentRepository.getNextNearestAppointments(clientId, fromDate);

        if (nextNearestAppointments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(nextNearestAppointments.get(0));
    }


}
