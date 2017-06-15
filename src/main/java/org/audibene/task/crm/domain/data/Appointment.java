package org.audibene.task.crm.domain.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
public class Appointment {

    private static final ZoneOffset DEFAULT_ZONE = ZoneOffset.UTC;

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(DEFAULT_ZONE));
    }
    public static Appointment onDate(LocalDateTime localDateTime) {
        return new Appointment(toDate(localDateTime));
    }

    public static Appointment forClientOnDate(Client client, LocalDateTime localDateTime) {
        return new Appointment(toDate(localDateTime), client);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentTime;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull
    private Client client;

    public Appointment() {}

    public Appointment(Date appointmentTime) {
        this(appointmentTime, null);
    }

    public Appointment(Date appointmentTime, Client client) {
        this.appointmentTime = appointmentTime;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
