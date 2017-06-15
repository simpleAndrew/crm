package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Query("Select a from Appointment a where a.client.id=:clientId order by a.appointmentTime desc")
    Iterable<Appointment> getLatestAppointment(@Param("clientId") Long clientId);
}
