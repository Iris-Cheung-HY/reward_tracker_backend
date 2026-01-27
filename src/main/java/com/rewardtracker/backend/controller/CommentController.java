package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.repository.CommentRepository;
import com.rewardtracker.backend.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository CommentRepository;

    @PostMapping("/comment")
    Comment newPost(@RequestBody Comment newComment) {
        
        return  CommentRepository.save(newComment);

    }

}
