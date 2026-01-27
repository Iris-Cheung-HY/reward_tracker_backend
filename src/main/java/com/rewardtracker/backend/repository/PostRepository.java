package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(String category);

    List<Post> findByIsFeaturedTrue();
}

