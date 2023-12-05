package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.EventDTO;
import com.example.SocialNetwork.entities.Attending;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.BadRequestException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.AttendingRepository;
import com.example.SocialNetwork.repository.EventRepository;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public EventDTO saveEvent(Event event) {
        List<Long> members=groupMemberRepository.findAllM(event.getSocialGroup().getId());
        for (Long id: members ) {
            if(id.equals(event.getUser().getId())){
                LocalDateTime localDateTime = LocalDateTime.now();
                Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                if(event.getDate().compareTo(currentDate) < 0){
                    throw new BadRequestException("Trying to insert an event with bad Date");
                }
                eventRepository.save(event);
                return mapper.map(event, EventDTO.class);
            }
        }
        throw new NotFoundException("User is not a member of this group");
    }

    @Override
    public List<EventDTO> getEventsBySocialGroup(Long id) {
        List<Event> events = eventRepository.findAllBySocialGroupId(id);
        if(events.isEmpty()) {
            throw new NotFoundException("There are no events associated with this social group");
        }
        return events.stream().map(event->mapper.map(event, EventDTO.class)).toList();
    }

    @Override
    public String confirmAttendance(Long id, User u) {
        if(u == null) {
            throw new NotFoundException("User not found");
        }
        if(!eventRepository.findById(id).isPresent()) {
            throw new NotFoundException("Event not found");
        }
        Event event = eventRepository.findById(id).get();
        List<Attending> all=u.getAttendings();
        for (Attending tmp : all) {
            if(tmp.getEvent().getId().equals(event.getId())){
                throw new BadRequestException("User already confirmed attendance");
            }
        }
        List<Long> members=groupMemberRepository.findAllM(event.getSocialGroup().getId());
        for (Long idMember: members ) {
            if (idMember.equals(u.getId())) {
                Attending attending = new Attending();
                attending.setEvent(event);
                attending.setUser(u);
                u.addAttending(attending);
                event.addAttending(attending);
                attendingRepository.save(attending);
                return "You successfully confirmed";
            }
        }
        throw new BadRequestException("You are not a member of this social group");
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
                    if(u.getDoNotDisturb()!=null && u.getDoNotDisturb().getTime()>currentDate.getTime())
                        continue;
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
}
