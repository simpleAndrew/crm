package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {


    @Query("Select a from Appointment a where a.client.id=:clientId order by a.appointmentTime asc")
    List<Appointment> getAllClientAppointments(@Param("clientId") Long clientId);

    @Query("Select a from Appointment a where a.client.id=:clientId AND a.appointmentTime >= :fromDate order by a.appointmentTime asc")
    List<Appointment> getNextAppointments(@Param("clientId") Long clientId, @Param("fromDate") Date from);

    @Query("Select a from Appointment a where a.client.id=:clientId AND a.appointmentTime <= :toDate order by a.appointmentTime desc")
    List<Appointment> getPreviousAppointments(@Param("clientId") Long clientId, @Param("toDate") Date toDate);

    @Query("select a from Appointment a where a.appointmentTime > :fromTime AND a.appointmentTime < :toTime order by a.appointmentTime asc")
    List<Appointment> findForPeriod(@Param("fromTime") Date from,
                                        @Param("toTime") Date to);
}
