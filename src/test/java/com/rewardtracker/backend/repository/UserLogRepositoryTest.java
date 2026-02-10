package com.rewardtracker.backend.repository;


import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection=EmbeddedDatabaseConnection.H2)

public class UserLogRepositoryTest {

    @Autowired
    private UserLogRepository userLogRepository;
    
    @Test
    public void UserLog_FindByUsername_ReturnedUser() {

        //Arrange

        UserLog user1 = new UserLog();
        user1.setUsername("iris01123");
        user1.setEmail("testiris@testiris123.com");

        //Act

        Optional<UserLog> res = userLogRepository.findByUsername("iris01123");


        //Assert

        Assertions.assertThat(res).isPresent();
        Assertions.assertThat(res.get().getUsername()).isEqualTo("iris01123");
    }



}
