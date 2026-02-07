package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.Post;
import com.rewardtracker.backend.repository.PostRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(String category) {
        if (category == null || category.isBlank()) {
            return postRepository.findAll();
        }
        return postRepository.findByCategoryOrderByCreatedAtDesc(category);
    }

    public Page<Post> getCatPosts(String category, Pageable pageable) {
        return postRepository.findByCategory(category, pageable);
    }

    public Page <Post> getFeaturedPosts(Pageable pageable) {
        return postRepository.findByIsFeaturedTrue(pageable);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}

