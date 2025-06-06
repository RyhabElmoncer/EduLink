package com.EduLink.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String name;
    private String description;
    private long dateTime;

    @DBRef
    private User organizer;

    @DBRef
    private List<User> participants;

}


