package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.model.Post;
import com.rewardtracker.backend.service.PostService;
import com.rewardtracker.backend.repository.UserLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private UserLogRepository userLogRepository;

    @Test
    public void PostController_NewPost_ReturnCreatedPost() throws Exception {

        // Arrange
        Post post = new Post();
        post.setTitle("My Test Post");
        post.setBody("This is the body");
        post.setCategory("Travel");

        when(postService.savePost(any(Post.class))).thenReturn(post);

        String reqLoad = "{"
            + "\"title\": \"My Test Post\","
            + "\"body\": \"This is the body\","
            + "\"category\": \"Travel\""
            + "}";


        // Act
        ResultActions response = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqLoad));


        // Assert
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.title").value("My Test Post"));
        response.andExpect(jsonPath("$.body").value("This is the body"));
        response.andExpect(jsonPath("$.category").value("Travel"));
    }
}