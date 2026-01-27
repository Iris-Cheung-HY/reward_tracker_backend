package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.repository.UserLogRepository;
import com.rewardtracker.backend.model.UserLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLogController {

    @Autowired
    private UserLogRepository userRepository;

    @PostMapping("/user")
    UserLog newUser(@RequestBody UserLog newUser) {
        
        return userRepository.save(newUser);

    }

    @GetMapping("/users")
    List<UserLog> getAllUsers() {
        return userRepository.findAll();
    }




}
