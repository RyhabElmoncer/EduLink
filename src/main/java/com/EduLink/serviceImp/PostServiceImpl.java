package com.EduLink.serviceImp;
import com.EduLink.Models.Post;
import com.EduLink.repository.PostRepository;
import com.EduLink.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post likePost(String postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow();
   //     post.getLikes().add(userId);
        return postRepository.save(post);
    }
}
