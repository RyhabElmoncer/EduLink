package com.EduLink.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String name;
    private String description;

    @DBRef
    private List<User> members = new ArrayList<>();

    @DBRef
    private List<Message> messages = new ArrayList<>();

    // Constructor to initialize only the ID
    public Group(String id) {
        this.id = id;
    }
}
