package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLogRepository extends JpaRepository <UserLog, Long> {

    Optional<UserLog> findByUsername(String username); 
}

