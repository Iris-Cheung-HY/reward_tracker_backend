package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.CommentService;
import com.rewardtracker.backend.model.Comment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app/")
@RestController
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/{postId}/comments")
    Comment newComment(@RequestBody Comment newComment, @PathVariable Long postId) {
        return commentService.saveComment(newComment);
    }

    @GetMapping("/{postId}/comments")
    public List<Comment> getAllCommentsByPostId(@PathVariable Long postId) {
        return commentService.getAllCommentsByPostId(postId);

    }

    @DeleteMapping("/{postId}/comments/{id}")
    public void deleteCommentById(@PathVariable Long postId, @PathVariable Long id) {
        commentService.deleteCommentById(id);
    }
}
