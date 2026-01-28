package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.UserLog;
import com.rewardtracker.backend.repository.UserLogRepository;

import java.util.List;

import org.springframework.stereotype.Service;



@Service

public class UserLogService {

    private final UserLogRepository userLogRepository;

    public UserLogService(UserLogRepository userLogRepository) {
        this.userLogRepository = userLogRepository;
    }

    public UserLog saveUser(UserLog userLog) {
        return userLogRepository.save(userLog);
    }

    public void deleteUserById(Long id) {
        userLogRepository.deleteById(id);
    }

    
    public List<UserLog> getAllUsers() {
        return userLogRepository.findAll();
    }
}
