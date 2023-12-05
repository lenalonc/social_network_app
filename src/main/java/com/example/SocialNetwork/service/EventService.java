package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.EventDTO;
import com.example.SocialNetwork.entities.Attending;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {

    EventDTO saveEvent(Event event);

    List<EventDTO> getEventsBySocialGroup(Long id);

    String confirmAttendance(Long id, User u);

    void notifyUsers();

    void deleteEvents();
}
