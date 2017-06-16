package org.audibene.task.crm.endpoints;

import org.audibene.task.crm.domain.AppointmentService;
import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.audibene.task.crm.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientsEndpoint {


    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClientRepository clientRepository;

    @GET
    public Response getClients() {
        return Response.ok()
                .entity(clientRepository.findAll())
                .build();
    }

    @GET
    @Path("{clientId}")
    public Response getClient(@PathParam("clientId") Long clientId) {
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            return Response.ok()
                    .entity(client)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createClient(Client client) {
        Client savedClient = clientRepository.save(client);
        return Response.ok()
                .entity(savedClient)
                .build();
    }

    @GET
    @Path("{clientId}/appointments")
    public Response getAppointments(@PathParam("clientId") Long clientId) {
        return Response.ok()
                .entity(appointmentService.getAppointmentsForClient(clientId))
                .build();
    }

    @POST
    @Path("{clientId}/appointments")
    public Response createAppointment(@PathParam("clientId") Long clientId, Appointment appointment) {
        Appointment createAppointment = appointmentService.createAppointment(clientId, appointment);
        return Response.ok()
                .entity(createAppointment)
                .build();
    }

    @GET
    @Path("{clientId}/appointments/next")
    public Response getUpcommingAppointment(@PathParam("clientId") Long clientId) {
        Optional<Appointment> nearestAppointment = appointmentService.getNearestAppointment(clientId, LocalDateTime.now());

        if (nearestAppointment.isPresent()) {
            return Response.ok()
                    .entity(nearestAppointment.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{clientId}/appointments/last")
    public Response getLastVisitedAppointment(@PathParam("clientId") Long clientId) {
        Optional<Appointment> nearestAppointment = appointmentService.getLastVisitedAppointment(clientId, LocalDateTime.now());

        if (nearestAppointment.isPresent()) {
            return Response.ok()
                    .entity(nearestAppointment.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
