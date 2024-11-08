package com.EduLink.service;
import com.EduLink.Models.Event;

import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    Event registerForEvent(String eventId, String userId);
}
