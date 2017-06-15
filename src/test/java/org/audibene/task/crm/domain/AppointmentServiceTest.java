package org.audibene.task.crm.domain;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
import org.audibene.task.crm.domain.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.audibene.task.crm.domain.data.Appointment.toDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnLatestAppointment() {
        //given
        Client client = clientRepository.save(Client.ofName("John Smith"));
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime appointmentInThePast = now.minusDays(1);
        LocalDateTime nearestAppointmentTime = appointmentInThePast.plusDays(1);
        LocalDateTime appointmentInTheFuture = appointmentInThePast.plusDays(10);

        appointmentRepository.save(Appointment.forClientOnDate(client, appointmentInThePast));
        appointmentRepository.save(Appointment.forClientOnDate(client, appointmentInTheFuture));
        Appointment expectedToBeLastAppointment =
                appointmentRepository.save(Appointment.forClientOnDate(client, nearestAppointmentTime));

        //when
        Iterable<Appointment> appointments = appointmentRepository.getNextNearestAppointments(client.getId(), toDate(now));

        //then
        assertThat(appointments).isNotEmpty();

        Appointment latestAppointment = appointments.iterator().next();

        assertThat(latestAppointment.getId()).isEqualTo(expectedToBeLastAppointment.getId());
        assertThat(latestAppointment.getAppointmentTime().getTime())
                .isEqualTo(expectedToBeLastAppointment.getAppointmentTime().getTime());
    }
}