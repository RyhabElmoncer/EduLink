package com.EduLink.DTO;

import lombok.Data;

import java.util.List;

@Data
public class EventDTO {
    private String name;
    private String description;
    private long dateTime;
    private String organizerId;
    private List<String> participantIds;
}

