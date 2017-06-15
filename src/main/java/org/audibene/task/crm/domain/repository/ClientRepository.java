package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

}
