package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.UserLogService;
import com.rewardtracker.backend.model.UserLog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Optional<UserLog> userOpt = userLogService.findByUsername(username);
    
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        UserLog user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Wrong password");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId()); 
        response.put("username", user.getUsername());
        response.put("token", "fake-jwt-for-demo");
        
        return ResponseEntity.ok(response);
}
    
    @PostMapping("/check-username")
    public Map<String, Boolean> checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        boolean isAvailable = !userLogService.findByUsername(username).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return response;
    }

    @GetMapping
    List<UserLog> getAllUsers() {
        return userLogService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userLogService.deleteUserById(id);
    }

}
