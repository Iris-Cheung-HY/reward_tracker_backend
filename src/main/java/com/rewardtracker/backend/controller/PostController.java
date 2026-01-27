package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.PostService;
import com.rewardtracker.backend.model.Post;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    Post newPost(@RequestBody Post newPost) {
        
        return postService.savePost(newPost);

    }

    @GetMapping
    List<Post> getAllPosts(@RequestParam(required = false) String category){
        return postService.getAllPosts(category);
    }

    @GetMapping("/featured")
    public List<Post> getFeaturePosts() {
        return postService.getFeaturedPosts();

    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
    }

    

}
