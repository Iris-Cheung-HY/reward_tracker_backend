package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.UserLog;
import com.rewardtracker.backend.repository.UserLogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional 
public class UserLogServiceTest {

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private UserLogRepository userLogRepository;

    @Test
    public void UserLogService_SaveUser_ShouldStoreInH2() {
        // Arrange
        UserLog user = new UserLog();
        user.setUsername("iris_jpa_test");
        user.setEmail("jpa@test.com");

        // Act
        UserLog savedUser = userLogService.saveUser(user);

        // Assert
        Assertions.assertThat(savedUser.getId()).isNotNull();
        
        Optional<UserLog> dbUser = userLogRepository.findByUsername("iris_jpa_test");
        Assertions.assertThat(dbUser).isPresent();
        Assertions.assertThat(dbUser.get().getEmail()).isEqualTo("jpa@test.com");
    }

    @Test
    public void UserLogService_GetUserById_ShouldReturnUser() {
        // Arrange
        UserLog user = new UserLog();
        user.setUsername("find_me");
        user.setEmail("find@test.com");
        UserLog persisted = userLogRepository.save(user);

        // Act
        Optional<UserLog> result = userLogService.getUserById(persisted.getId());

        // Assert
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getUsername()).isEqualTo("find_me");
    }
}