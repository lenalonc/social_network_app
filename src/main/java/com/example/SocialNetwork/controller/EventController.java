package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.EventDTO;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private EventService eventService;
    private ModelMapper mapper;

    public EventController(EventService eventService, ModelMapper mapper) {
        this.eventService = eventService;
        this.mapper = mapper;
    }

    @PostMapping("/save")
    public Event saveEvent(@RequestBody Event event){
        eventService.saveEvent(event);
        return null;
    }
    @GetMapping("/{id}")
    public List<EventDTO> getEventsBySocialGroup(@PathVariable Long id){
        List<EventDTO> events=eventService.getEventsBySocialGroup(id);
        return events;
    }
}
