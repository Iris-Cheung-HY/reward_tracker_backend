package com.rewardtracker.backend.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;


@Entity
public class UserLog {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    private LocalDateTime createdAt; 

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}
