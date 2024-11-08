package com.EduLink.controller;
import com.EduLink.Models.Post;
import com.EduLink.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/{postId}/like")
    public Post likePost(@PathVariable String postId, @RequestParam String userId) {
        return postService.likePost(postId, userId);
    }
}
