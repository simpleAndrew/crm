package org.audibene.task.crm.domain.repository;

import org.audibene.task.crm.domain.data.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static junit.framework.TestCase.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test(expected = ConstraintViolationException.class)
    public void shouldValidateThatNameIsMissing() {
        //given
        Client noNameClient = new Client();

        //when
        clientRepository.save(noNameClient);

        fail("Should throw exception on missing name");
    }

}