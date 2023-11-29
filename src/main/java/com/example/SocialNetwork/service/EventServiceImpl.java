package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.EventDTO;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService{
    private EventRepository eventRepository;
    private ModelMapper mapper;

    public EventServiceImpl(EventRepository eventRepository, ModelMapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Override
    public void saveEvent(Event event) {
        this.eventRepository.save(event);
    }

    @Override
    public List<EventDTO> getEventsBySocialGroup(Long id) {
        return this.eventRepository.findAllBySocialGroupId(id).stream().map(event->mapper.map(event, EventDTO.class)).toList();
    }
}
