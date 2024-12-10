package com.EduLink.repository;

import com.EduLink.Models.Event;
import com.EduLink.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByOrganizerId(String organizerId);
    List<Event> findByParticipantsContains(User participant);
}

