package com.EduLink.Models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String title;
    private String description;
    private String date;
    private List<String> participants;

    // Getters et Setters
}
