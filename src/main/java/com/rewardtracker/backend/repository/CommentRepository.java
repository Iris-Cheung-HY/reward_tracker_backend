
package com.rewardtracker.backend.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rewardtracker.backend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long> {
    List<Comment> findCommentByPostId (Long postId);

}
