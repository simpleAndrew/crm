package org.audibene.task.crm.endpoints;

import org.audibene.task.crm.domain.AppointmentService;
import org.audibene.task.crm.domain.data.Appointment;
import org.audibene.task.crm.domain.data.Client;
import org.audibene.task.crm.domain.repository.AppointmentRepository;
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
import java.util.Collections;
import java.util.Optional;

@Controller
@Path("/clients")
public class ClientsEndpoint {


    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response clientList() {
        return Response.status(Response.Status.OK)
                .entity(clientRepository.findAll())
                .build();
    }

    @GET
    @Path("{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clientList(@PathParam("clientId") Long clientId) {
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            return Response.status(Response.Status.OK)
                    .entity(client)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createClient(Client client) {
        Client savedClient = clientRepository.save(client);
        return Response.status(Response.Status.OK)
                .entity(savedClient)
                .build();
    }

    @GET
    @Path("{clientId}/appointments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointments(@PathParam("clientId") String clientId) {
        return Response.status(Response.Status.OK)
                .entity(Collections.emptyList())
                .build();
    }

    @POST
    @Path("{clientId}/appointments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAppointment(@PathParam("clientId") Long clientId, Appointment appointment) {
        Appointment save = appointmentRepository.save(appointment);
        return Response.status(Response.Status.OK)
                .entity(save)
                .build();
    }

    @GET
    @Path("{clientId}/appointments/next")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestAppointment(@PathParam("clientId") Long clientId) {
        Optional<Appointment> nearestAppointment = appointmentService.getNearestAppointment(clientId);

        if (nearestAppointment.isPresent()) {
            return Response.status(Response.Status.OK)
                    .entity(nearestAppointment.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
