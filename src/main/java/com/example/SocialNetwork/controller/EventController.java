package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.EventDTO;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.service.EventService;
import com.example.SocialNetwork.service.GroupMemberService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private EventService eventService;
    private GroupMemberService groupMemberService;
    private ModelMapper mapper;

    public EventController(EventService eventService, GroupMemberService groupMemberService, ModelMapper mapper) {
        this.eventService = eventService;
        this.groupMemberService = groupMemberService;
        this.mapper = mapper;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveEvent(@RequestBody Event event){
//        List<Long> members=groupMemberService.getAllGroupMembers(event.getSocialGroup().getId());
//        for (Long id: members ) {
//            if(id==event.getUser().getId()){
//                eventService.saveEvent(event);
//                LocalDateTime localDateTime = LocalDateTime.now();
//                Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//                if(event.getDate().compareTo(currentDate) < 0){
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trying to insert an event with bad Date");
//                }
//                return ResponseEntity.status(HttpStatus.OK).body("Event successfully created");
//            }
//
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not a member of this social group");
        return null;
    }
    @GetMapping("/{id}")
    public List<EventDTO> getEventsBySocialGroup(@PathVariable Long id){
        List<EventDTO> events=eventService.getEventsBySocialGroup(id);
        return events;
    }
}
