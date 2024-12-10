package com.EduLink.service;

import com.EduLink.DTO.EventDTO;
import com.EduLink.Exceptions.ResourceNotFoundException;
import com.EduLink.Models.Event;
import com.EduLink.Models.User;
import com.EduLink.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = Event.builder()
                .name(eventDTO.getName())
                .description(eventDTO.getDescription())
                .dateTime(eventDTO.getDateTime())
                .organizer(new User(eventDTO.getOrganizer().getId()))
                .participants(eventDTO.getParticipants().stream().map(u -> new User(u.getId())).collect(Collectors.toList()))
                .build();
        event = eventRepository.save(event);
        return new EventDTO(event);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(EventDTO::new).collect(Collectors.toList());
    }

    public void signUpForEvent(String eventId, String userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        event.getParticipants().add(new User(userId));
        eventRepository.save(event);
    }

}

