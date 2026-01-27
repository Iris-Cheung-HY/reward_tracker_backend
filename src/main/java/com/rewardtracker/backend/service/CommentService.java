package com.rewardtracker.backend.service;

import org.springframework.stereotype.Service;
import com.rewardtracker.backend.model.Comment;
import com.rewardtracker.backend.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.findCommentByPostId(postId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);

    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

}
