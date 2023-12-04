package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.EventDTO;
import com.example.SocialNetwork.dtos.SocialGroupDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.Event;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.AttendingRepository;
import com.example.SocialNetwork.repository.EventRepository;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.EmailService;
import com.example.SocialNetwork.service.EventService;
import com.example.SocialNetwork.service.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private GroupMemberRepository groupMemberRepository;
    private AttendingRepository attendingRepository;
    private EmailService emailService;
    private ModelMapper mapper;
    private EventService eventService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.eventRepository = mock(EventRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.groupMemberRepository = mock(GroupMemberRepository.class);
        this.attendingRepository = mock(AttendingRepository.class);
        this.emailService = mock(EmailService.class);
        this.mapper = new ModelMapper();
        this.eventService = new EventServiceImpl(eventRepository, userRepository, groupMemberRepository, attendingRepository, emailService, mapper);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void saveEventUserNotAMember() {
        User user = User.builder()
                .id(235L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user)
                .type(true)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(new Date())
                .place("Belgrade")
                .user(user)
                .socialGroup(socialGroup)
                .build();

        //EventDTO event2 = new EventDTO(1L, new Date(), "Belgrade", new UserDTO(1L, "email@gmail.com", "Test", true, new Date()), new SocialGroupDTO(1L, "Volimo pse", true, new UserDTO(1L, "email@gmail.com", "Test", true, new Date())));

        when(eventRepository.saveAndFlush(any())).thenReturn(event);

        var result = eventService.saveEvent(mapper.map(event, Event.class));

        assertEquals("Belgrade", result.getPlace());

        verify(eventRepository, times(1)).saveAndFlush(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void getEventsBySocialGroup() {
    }

    @Test
    void confirmAttendance() {
    }
}