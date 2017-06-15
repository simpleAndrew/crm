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
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentService appointmentService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnNearestUpcomingAppointment() {
        //given
        Client client = clientRepository.save(Client.ofName("John Smith"));
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime appointmentInThePast = now.minusDays(1);
        LocalDateTime nearestAppointmentTime = now.plusDays(1);
        LocalDateTime appointmentInTheFuture = now.plusDays(10);

        appointmentRepository.save(Appointment.forClientOnDate(client, appointmentInThePast));
        appointmentRepository.save(Appointment.forClientOnDate(client, appointmentInTheFuture));
        Appointment expectedToBeLastAppointment =
                appointmentRepository.save(Appointment.forClientOnDate(client, nearestAppointmentTime));

        //when
        Optional<Appointment> latestAppointment = appointmentService.getNearestAppointment(client.getId(), now);

        //then
        assertThat(latestAppointment.isPresent()).isTrue();
        assertThat(latestAppointment.get().getId()).isEqualTo(expectedToBeLastAppointment.getId());
        assertThat(latestAppointment.get().getAppointmentTime().getTime())
                .isEqualTo(expectedToBeLastAppointment.getAppointmentTime().getTime());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnLastVisitedAppointment() {
        //given
        Client client = clientRepository.save(Client.ofName("John Smith"));
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime appointmentInThePast = now.minusDays(2);
        LocalDateTime tooFarInThePastAppointment = now.minusDays(10);
        LocalDateTime nearestAppointmentTime = now.plusDays(1);
        LocalDateTime appointmentInTheFuture = now.plusDays(10);

        appointmentRepository.save(Appointment.forClientOnDate(client, appointmentInTheFuture));
        appointmentRepository.save(Appointment.forClientOnDate(client, nearestAppointmentTime));
        appointmentRepository.save(Appointment.forClientOnDate(client, tooFarInThePastAppointment));
        Appointment lastVisitedExpected = appointmentRepository.save(Appointment.forClientOnDate(client, appointmentInThePast));

        //when
        Optional<Appointment> lastVisited = appointmentService.getLastVisitedAppointment(client.getId(), now);

        //then
        assertThat(lastVisited.isPresent()).isTrue();
        assertThat(lastVisited.get().getId()).isEqualTo(lastVisitedExpected.getId());
        assertThat(lastVisited.get().getAppointmentTime().getTime())
                .isEqualTo(lastVisitedExpected.getAppointmentTime().getTime());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnAppointmentsFromGivenPeriod() {
        //given
        Client client = clientRepository.save(Client.ofName("Magic Johnson"));
        LocalDateTime now = LocalDateTime.now();

        List<Long> expectedAppointmentIds = IntStream.range(1, 6)
                .mapToObj(now::plusDays)
                .map(date -> Appointment.forClientOnDate(client, date))
                .map(appointmentRepository::save)
                .map(Appointment::getId)
                .collect(toList());

        //when
        Iterable<Appointment> forPeriod = appointmentService.getNextWeekAppointments(LocalDateTime.now());
        Stream<Appointment> stream = StreamSupport.stream(forPeriod.spliterator(), false);
        List<Long> returnedAppointmentIds = stream
                .map(Appointment::getId)
                .collect(toList());

        //then
        assertThat(returnedAppointmentIds).isEqualTo(expectedAppointmentIds);
    }
}