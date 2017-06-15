package org.audibene.task.crm.endpoints.config;

import org.audibene.task.crm.endpoints.AppointmentEndpoint;
import org.audibene.task.crm.endpoints.ClientsEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class RestConfig extends ResourceConfig {

    public RestConfig() {
        register(ClientsEndpoint.class);
        register(AppointmentEndpoint.class);
    }
}
