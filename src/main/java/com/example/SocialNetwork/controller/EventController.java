package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dtos.EventDTO;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private EventService eventService;
    private UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveEvent(@RequestBody Event event){
        return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventsBySocialGroup(@PathVariable Long id){
        List<EventDTO> events=eventService.getEventsBySocialGroup(id);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    @PostMapping("/attend/{id}")
    public ResponseEntity<?> confirmAttendance(@PathVariable Long id) {
        User currentUser = userService.findCurrentUser();
        return new ResponseEntity<>(eventService.confirmAttendance(id, currentUser), HttpStatus.OK);
    }

    @Scheduled(cron = "0 */1 * * * *") // 5 minutes
    public void notifyUsers(){
        this.eventService.notifyUsers();
    }
    @Scheduled(cron = "0 */1 * * * *") // 1 minute
    public void deleteEvents(){
        this.eventService.deleteEvents();
    }
}
