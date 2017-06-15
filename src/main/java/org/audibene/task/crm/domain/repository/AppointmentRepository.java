package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Query("Select a from Appointment a where a.client.id=:clientId order by a.appointmentTime desc")
    Iterable<Appointment> getLatestAppointment(@Param("clientId") Long clientId);

    @Query("select a from Appointment a where a.appointmentTime > :fromTime AND a.appointmentTime < :toTime order by a.appointmentTime asc")
    Iterable<Appointment> findForPeriod(@Param("fromTime") Date from,
                                        @Param("toTime") Date to);
}
