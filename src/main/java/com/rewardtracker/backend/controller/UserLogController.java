package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.UserLogService;
import com.rewardtracker.backend.model.UserLog;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserLogController {

    private final UserLogService userLogService;

    public UserLogController(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @PostMapping
    UserLog newUser(@RequestBody UserLog newUser) {
        
        return userLogService.saveUser(newUser);

    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userLogService.deleteUserById(id);
    }

}
