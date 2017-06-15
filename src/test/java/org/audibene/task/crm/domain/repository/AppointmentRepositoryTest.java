package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;

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
        Appointment appointment = new Appointment(appTime);

        //when
        Appointment savedAppointment = appointmentRepository.save(appointment);

        //then
        assertThat(savedAppointment.getId()).isNotNull();
    }

    @Test
    public void shouldCreateConnectionWithClient() {
        //given
        Client clientToGetAppointment = new Client("John Smith");
        Client persistedClient = clientRepository.save(clientToGetAppointment);

        Appointment appointment = new Appointment(LocalDateTime.now());
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

}