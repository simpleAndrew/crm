package org.audibene.task.crm.endpoints;

import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentEndpointTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldComplainAboutTimeMissing() throws Exception {
        //given
        Appointment request = Appointment.forClientOnDate(Client.idOnly(1L), LocalDateTime.now());

        //when
        ResponseEntity responseEntity = testRestTemplate.postForEntity("/appointments", request, null);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldComplainAboutClientMissing() throws Exception {
        //given
        Appointment request = Appointment.forClientOnDate(Client.idOnly(1L), LocalDateTime.now());

        //when
        ResponseEntity responseEntity = testRestTemplate.postForEntity("/appointments", request, null);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnEmptyListOfAppointments() {
        //when
        ResponseEntity<List> forEntity =
                testRestTemplate.getForEntity("/appointments", List.class);

        //then
        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).isEmpty();
    }
}