package com.EduLink.Models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String content;
    private String imageUrl;
    private String userId; // Référence à l'auteur de la publication
    private List<String> tags;
    private List<String> likes;
    private List<Comment> comments;

    // Getters et Setters

    public static class Comment {
        private String userId;
        private String content;

        // Getters et Setters
    }
}
