package org.audibene.task.crm.endpoints;

import org.audibene.task.crm.domain.AppointmentService;
import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@Path("appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentEndpoint {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentService appointmentService;

    @GET
    public Response getAllAppointments() {
        Iterable<Appointment> all = appointmentRepository.findAll();
        return Response.ok()
                .entity(all)
                .build();
    }

    @GET
    @Path("/{appointmentId}")
    public Response getAppointment(@PathParam("appointmentId") Long appointmentId) {
        Appointment appointment = appointmentRepository.findOne(appointmentId);
        if(appointment != null) {
            return Response.ok()
                    .entity(appointment)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    public Response createAppointment(Appointment appointment) {
        Appointment appointmentCreated = appointmentRepository.save(appointment);
        return Response.ok()
                .entity(appointmentCreated)
                .build();
    }

    @GET
    @Path("/nextWeek")
    public Response getNextWeekAppointments() {
        return Response.ok()
                .entity(appointmentService.getNextWeekAppointments(LocalDateTime.now()))
                .build();
    }

    @PUT
    @Path("/{appointmentId}/rate")
    public Response rateAppintment(@PathParam("appointmentId") Long appointmentId, @QueryParam("rating") String rating) {
        Optional<Appointment> ratedAppointment = appointmentService.rateAppointment(appointmentId, rating);

        if (ratedAppointment.isPresent()) {
            return Response.ok()
                    .entity(ratedAppointment.get())
                    .build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
