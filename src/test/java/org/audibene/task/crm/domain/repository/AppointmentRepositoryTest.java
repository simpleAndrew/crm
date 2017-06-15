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
import static org.audibene.task.crm.domain.data.Appointment.toDate;

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
        Client client = clientRepository.save(Client.ofName("Harry Potter"));
        Appointment appointment = Appointment.forClientOnDate(client, LocalDateTime.now());

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
        Client clientToGetAppointment = clientRepository.save(Client.ofName("John Smith"));
        Appointment appointment = new Appointment(null, clientToGetAppointment);

        //when
        Appointment save = appointmentRepository.save(appointment);

        //then
        fail("Validation exception should be thrown");
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldNotAcceptEmptyClient() {
        //given
        Appointment appointment = Appointment.onDate(LocalDateTime.now());

        //when
        Appointment save = appointmentRepository.save(appointment);

        //then
        fail("Validation exception should be thrown");
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

        Date from = toDate(now.minusDays(1));
        Date to = toDate(now.plusDays(7));

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