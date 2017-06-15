package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long>, PagingAndSortingRepository<Appointment, Long> {
    default Appointment getLatestAppointment(Long clientId) {
        return null;
    }
}
