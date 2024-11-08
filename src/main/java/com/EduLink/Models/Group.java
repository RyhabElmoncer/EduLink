package com.EduLink.Models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> members;

    // Getters et Setters
}
