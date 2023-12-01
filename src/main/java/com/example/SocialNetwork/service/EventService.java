package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.EventDTO;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface EventService {

    ResponseEntity<?> saveEvent(Event event);

    List<EventDTO> getEventsBySocialGroup(Long id);

    ResponseEntity<?> confirmAttendance(Long id, User u);

    void notifyUsers();

    void deleteEvents();
    Event findEventById(Long id);

    List<Event> findAll();

    void deleteById(Long id);
}
