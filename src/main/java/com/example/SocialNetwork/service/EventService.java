package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.EventDTO;
import com.example.SocialNetwork.entities.Event;

import java.util.List;

public interface EventService {

    void saveEvent(Event event);

    List<EventDTO> getEventsBySocialGroup(Long id);
}
