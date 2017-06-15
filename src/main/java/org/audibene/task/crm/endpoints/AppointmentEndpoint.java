package org.audibene.task.crm.endpoints;

import org.audibene.task.crm.domain.AppointmentService;
import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Controller
@Path("appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentEndpoint {

    @Autowired
    private AppointmentRepository appointmentRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAppointments() {
        Iterable<Appointment> all = appointmentRepository.findAll();
        return Response.status(Response.Status.OK)
                .entity(all)
                .build();
    }

    @POST
    public Response createAppointment(Appointment appointment) {
        Appointment appointmentCreated = appointmentRepository.save(appointment);
        return Response.status(Response.Status.OK)
                .entity(appointmentCreated)
                .build();
    }

    @GET
    @Path("/next7days")
    public Response getNextWeekAppointments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAfter = now.plusWeeks(1L);
        Iterable<Appointment> forPeriod = appointmentRepository.findForPeriod(Appointment.toDate(now), Appointment.toDate(weekAfter));

        return Response.status(Response.Status.OK)
                .entity(forPeriod)
                .build();
    }
}
