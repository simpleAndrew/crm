package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void shouldGenerateId() {
        //given
        LocalDateTime appTime = LocalDateTime.now();
        Appointment appointment = Appointment.onDate(appTime);

        //when
        Appointment savedAppointment = appointmentRepository.save(appointment);

        //then
        assertThat(savedAppointment.getId()).isNotNull();
    }

    @Test
    public void shouldCreateConnectionWithClient() {
        //given
        Client clientToGetAppointment = Client.ofName("John Smith");
        Client persistedClient = clientRepository.save(clientToGetAppointment);

        Appointment appointment = Appointment.onDate(LocalDateTime.now());
        appointment.setClient(Client.idOnly(persistedClient.getId()));

        //when
        Appointment savedAppointment = appointmentRepository.save(appointment);
        Appointment foundAppointment = appointmentRepository.findOne(savedAppointment.getId());

        //then
        assertThat(foundAppointment.getClient()).isEqualTo(persistedClient);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldNotAcceptEmptyDate() {
        //given
        Appointment appointment = new Appointment();

        //when
        Appointment save = appointmentRepository.save(appointment);

        //then
        fail("Validation exception should be thrown");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnLatestAppointment() {
        //given
        Client client = clientRepository.save(Client.ofName("John Smith"));
        LocalDateTime timeOfFirstAppointment = LocalDateTime.now();
        LocalDateTime timeOfLatestAppointment = timeOfFirstAppointment.plusDays(1);

        appointmentRepository.save(Appointment.forClientOnDate(client, timeOfFirstAppointment));

        Appointment expectedToBeLastAppointment =
                appointmentRepository.save(Appointment.forClientOnDate(client, timeOfLatestAppointment));

        //when
        Iterable<Appointment> appointments = appointmentRepository.getLatestAppointment(client.getId());

        //then
        assertThat(appointments).isNotEmpty();

        Appointment latestAppointment = appointments.iterator().next();

        assertThat(latestAppointment.getId()).isEqualTo(expectedToBeLastAppointment.getId());
        assertThat(latestAppointment.getAppointmentTime().getTime()).isEqualTo(expectedToBeLastAppointment.getAppointmentTime().getTime());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnAppointmentsFromGivenPeriod() {
        //given
        Client client = clientRepository.save(Client.ofName("Magic Johnson"));
        LocalDateTime now = LocalDateTime.now();

        List<Long> expectedAppointmentIds = IntStream.range(0, 6)
                .mapToObj(now::plusDays)
                .map(date -> Appointment.forClientOnDate(client, date))
                .map(appointmentRepository::save)
                .map(Appointment::getId)
                .collect(toList());

        Date from = Appointment.toDate(now.minusDays(1));
        Date to = Appointment.toDate(now.plusDays(7));

        //when
        Iterable<Appointment> forPeriod = appointmentRepository.findForPeriod(from, to);
        Stream<Appointment> stream = StreamSupport.stream(forPeriod.spliterator(), false);
        List<Long> returnedAppointmentIds = stream
                .map(Appointment::getId)
                .collect(toList());

        //then
        assertThat(returnedAppointmentIds).isEqualTo(expectedAppointmentIds);
    }

}