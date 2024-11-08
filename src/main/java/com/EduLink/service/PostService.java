package com.EduLink.service;
import com.EduLink.Models.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post);
    List<Post> getAllPosts();
    Post likePost(String postId, String userId);
}
