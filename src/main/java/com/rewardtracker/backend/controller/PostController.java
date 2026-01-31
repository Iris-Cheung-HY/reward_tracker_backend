package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.PostService;
import com.rewardtracker.backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app/")
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

    @GetMapping("/travel-preview")
    public Page<Post> getTravelPreview() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        return postService.getCatPosts("Travel", pageable);
    }

    @GetMapping("/creditcard-preview")
    public Page<Post> getCreditCardPreview() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        return postService.getCatPosts("Credit Card", pageable);
    }

    @GetMapping("/travel")
        public List<Post> getAllTravel() {
        return postService.getAllPosts("Travel");
    }

    @GetMapping("/creditcard")
        public List<Post> getAllCreditCard() {
        return postService.getAllPosts("Credit Card");
    }


    @GetMapping("/featured")
    public Page <Post> getFeaturePosts() {
        Pageable pageable = PageRequest.of(0,3, Sort.by("createdAt").descending());
        return postService.getFeaturedPosts(pageable);

    }

    // Why need this one
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
    }



}
