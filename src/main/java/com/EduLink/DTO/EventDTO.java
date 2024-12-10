package com.EduLink.DTO;

import com.EduLink.Models.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {
    private String id;
    private String name;
    private String description;
    private long dateTime;
    private UserDTO organizer;
    private List<UserDTO> participants;
    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.dateTime = event.getDateTime();
        this.organizer = new UserDTO(event.getOrganizer());
        this.participants = event.getParticipants().stream().map(UserDTO::new).collect(Collectors.toList());
    }
}


