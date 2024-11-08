package com.EduLink.repository;
import com.EduLink.Models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByTagsContains(String tag);
}
