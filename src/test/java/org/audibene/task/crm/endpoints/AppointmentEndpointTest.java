package org.audibene.task.crm.endpoints;

import org.audibene.task.crm.domain.data.Appointment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCompainAboutTimeMissing() throws Exception {
        //given
        Appointment request = new Appointment();

        //when
        ResponseEntity<Appointment> responseEntity =
                restTemplate.postForEntity("/appointments", request, Appointment.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(500);
    }
}