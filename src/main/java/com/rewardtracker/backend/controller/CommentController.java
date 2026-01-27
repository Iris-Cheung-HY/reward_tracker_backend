package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.CommentService;
import com.rewardtracker.backend.model.Comment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/posts/id")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/comment")
    Comment newComment(@RequestBody Comment newComment) {
        return commentService.saveComment(newComment);
    }

    @GetMapping("/comment")
    List<Comment> getAllCommentByPostId (@RequestParam(required = true) Long post_id) {
        return commentService.getAllCommentByPostId(post_id);

    }

    @DeleteMapping("/{id}")
    public void deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
    }
 }
