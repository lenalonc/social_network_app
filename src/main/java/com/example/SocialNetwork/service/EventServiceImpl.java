package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.EventDTO;
import com.example.SocialNetwork.entities.Attending;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.AttendingRepository;
import com.example.SocialNetwork.repository.EventRepository;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EventServiceImpl implements EventService{
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private GroupMemberRepository groupMemberRepository;
    private AttendingRepository attendingRepository;

    private EmailService emailService;
    private ModelMapper mapper;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository, AttendingRepository attendingRepository, EmailService emailService, ModelMapper mapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.attendingRepository = attendingRepository;
        this.emailService = emailService;
        this.mapper = mapper;
    }


    @Override
    public ResponseEntity<?> saveEvent(Event event) {
        List<Long> members=groupMemberRepository.findAllBySocialGroupId(event.getSocialGroup().getId());
        for (Long id: members ) {
            if(id == event.getUser().getId()){
                LocalDateTime localDateTime = LocalDateTime.now();
                Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                if(event.getDate().compareTo(currentDate) < 0){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trying to insert an event with bad Date");
                }
                this.eventRepository.save(event);
                return ResponseEntity.status(HttpStatus.OK).body("Event successfully created");
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not a member of this social group");
    }

    @Override
    public List<EventDTO> getEventsBySocialGroup(Long id) {
        return this.eventRepository.findAllBySocialGroupId(id).stream().map(event->mapper.map(event, EventDTO.class)).toList();
    }

    @Override
    public ResponseEntity<?> confirmAttendance(Long id, User u) {
        if(u == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Error");
        }
        Event event = eventRepository.findById(id).get();
        if(event == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event Error");
        }
        List<Attending> all=u.getAttendings();
        for (Attending tmp : all) {
            if(tmp.getEvent().getId()==event.getId()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already confirmed attendance");

            }
        }
        List<Long> members=groupMemberRepository.findAllBySocialGroupId(event.getSocialGroup().getId());
        for (Long idMember: members ) {
            if (idMember == u.getId()) {
                Attending attending = new Attending();
                attending.setEvent(event);
                attending.setUser(u);
                u.addAttending(attending);
                event.addAttending(attending);
                attendingRepository.save(attending);
                return ResponseEntity.status(HttpStatus.OK).body("Confirmed!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not a member of this social group");
    }

    @Override
    public void notifyUsers() {
        List<Event> events=eventRepository.findAll();
        for (Event event : events) {
            Date date=event.getDate();
            LocalDateTime localDateTime = LocalDateTime.now();
            Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            long timeInMillis=date.getTime()-currentDate.getTime();
            long minutes= TimeUnit.MILLISECONDS.toMinutes(timeInMillis)%60;
            if( minutes>0 && minutes<15){
                List<Attending> attendings=attendingRepository.findAllByEventId(event.getId());
                for (Attending a : attendings) {
                    User u=a.getUser();
                    String text= "Event " + event.getId() + "is starting in less than 15 minutes";
                    String subject="Event reminder";
                    emailService.sendEmail(u.getEmail(), subject, text);
                }
            }

        }
    }

    @Override
    public void deleteEvents() {
        List<Event> events=eventRepository.findAll();
        long n=events.size();
        for(long i = 0; i < n; i++) {
            Event e=events.remove(0);
            Date date=e.getDate();
            LocalDateTime localDateTime = LocalDateTime.now();
            Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            long timeInMillis=date.getTime()-currentDate.getTime();
            long minutes= TimeUnit.MILLISECONDS.toMinutes(timeInMillis)%60;
            if(minutes<-15){
                eventRepository.deleteById(e.getId());
            }
            else{
                events.add(e);
            }
        }
    }

    @Override
    public Event findEventById(Long id) {
        return this.eventRepository.findById(id).get();
    }

    @Override
    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
    this.eventRepository.deleteById(id);
    }
}
