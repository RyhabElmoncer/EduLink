package com.EduLink.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "publications")
public class Publication {
    @Id
    private String id;
    private String userId;
    private String textContent;
    private List<String> tags;
    private String imageUrl;
    private List<String> likes;
    private List<Comment> comments;
    private long timestamp;
    @DBRef // Référence explicite vers l'utilisateur
    private User user;
}
