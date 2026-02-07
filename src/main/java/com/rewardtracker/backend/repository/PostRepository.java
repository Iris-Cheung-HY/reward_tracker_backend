package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByCreatedAtDesc(String category);
  
    Page<Post> findByCategory(String category, Pageable pageable);

    Page<Post> findByIsFeaturedTrue(Pageable pageable);
}

