package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.repository.*;
import com.rewardtracker.backend.service.*;
import com.rewardtracker.backend.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final UserLogRepository userLogRepository;

    private final PostService postService;

    public PostController(PostService postService, UserLogRepository userLogRepository) {
        this.postService = postService;
        this.userLogRepository = userLogRepository;
    }

    @PostMapping
    public Post newPost(@RequestBody java.util.Map<String, Object> payload) {
        Post post = new Post();
        post.setTitle((String) payload.get("title"));
        post.setBody((String) payload.get("body"));
        post.setCategory((String) payload.get("category"));
        post.setImageUrl((String) payload.get("imageUrl"));
        post.setIsFeatured(false); 

        if (payload.get("user_id") != null) {
            Long userId = Long.valueOf(payload.get("user_id").toString());
            userLogRepository.findById(userId).ifPresent(post::setUser);
        }

        return postService.savePost(post);
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
