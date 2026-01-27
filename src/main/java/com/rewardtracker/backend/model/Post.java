package com.rewardtracker.backend.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class Post {
    
    @Id
    @GeneratedValue

    private Long id;

    private String category;

    private String title;

    private String body;

    private Boolean isFeatured;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserLog user;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}
