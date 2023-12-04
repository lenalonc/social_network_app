package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.EventDTO;
import com.example.SocialNetwork.dtos.SocialGroupDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.BadRequestException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.AttendingRepository;
import com.example.SocialNetwork.repository.EventRepository;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.EmailService;
import com.example.SocialNetwork.service.EventService;
import com.example.SocialNetwork.service.EventServiceImpl;
import com.example.SocialNetwork.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

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
    void saveEventSuccess() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn(List.of(user1.getId()));
        var result = eventService.saveEvent(mapper.map(event, Event.class));

        assertEquals("Belgrade", result.getPlace());

        verify(eventRepository, times(1)).save(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
    }
    @Test
    void saveEventThrowsBadRequestException() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2021, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .socialGroup(socialGroup)
                .build();
        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn(List.of(user1.getId()));
        assertThrows(BadRequestException.class, ()->eventService.saveEvent(event));
    }
    @Test
    void saveEventThrowsNotFoundException() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2021, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("user2_email@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user2)
                .socialGroup(socialGroup)
                .build();
        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn(List.of(user1.getId()));
        assertThrows(NotFoundException.class, ()->eventService.saveEvent(event));
    }
    @Test
    void getEventsBySocialGroupSuccess() {
        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("user2_email@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();
        GroupMember groupMember2=GroupMember.builder()
                .id(7L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user2)
                .socialGroup(socialGroup)
                .build();

        Event event1 = Event.builder()
                .id(1L)
                .date(new Date(2024,12,12,5,5,5))
                .place("Belgrade")
                .user(user1)
                .socialGroup(socialGroup)
                .build();
        Event event2 = Event.builder()
                .id(2L)
                .date(new Date(2024,12,12,5,5,5))
                .place("Novi Sad")
                .user(user2)
                .socialGroup(socialGroup)
                .build();
        when(eventRepository.findAllBySocialGroupId(socialGroup.getId())).thenReturn(List.of(event1,event2));
        var result=eventService.getEventsBySocialGroup(socialGroup.getId());
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    void getEventsBySocialThrowsGroupNotFoundException() {
            User user1 = User.builder()
                    .id(1L)
                    .email("user1_email@gmail.com")
                    .username("TestUser1")
                    .password(passwordEncoder.encode("user1password"))
                    .active(true)
                    .build();
            User user2 = User.builder()
                    .id(2L)
                    .email("user2_email@gmail.com")
                    .username("TestUser2")
                    .password(passwordEncoder.encode("user2password"))
                    .active(true)
                    .build();
            SocialGroup socialGroup = SocialGroup.builder()
                    .id(219L)
                    .name("Group2")
                    .user(user1)
                    .type(true)
                    .build();
            GroupMember groupMember1=GroupMember.builder()
                    .id(6L)
                    .dateJoined(new Date(System.currentTimeMillis()))
                    .user(user1)
                    .socialGroup(socialGroup)
                    .build();
            GroupMember groupMember2=GroupMember.builder()
                    .id(7L)
                    .dateJoined(new Date(System.currentTimeMillis()))
                    .user(user2)
                    .socialGroup(socialGroup)
                    .build();
        assertThrows(NotFoundException.class, ()->eventService.getEventsBySocialGroup(socialGroup.getId()));
    }
    @Test
    void confirmAttendanceSuccess() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();

        List<Attending> attendings=new ArrayList<>();
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn(List.of(user1.getId()));
        var result=eventService.confirmAttendance(event.getId(), user1);
        assertEquals("You successfully confirmed", result);
    }
    @Test
    void confirmAttendanceThrowsNotFoundException1() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();
        Attending attending=Attending.builder()
                .id(1L)
                .event(event)
                .user(user1)
                .build();
        List<Attending> attendings=new ArrayList<>();
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        assertThrows(NotFoundException.class, ()->eventService.confirmAttendance(event.getId(), null));
    }
    @Test
    void confirmAttendanceThrowsNotFoundException2() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();
        Attending attending=Attending.builder()
                .id(1L)
                .event(event)
                .user(user1)
                .build();
        List<Attending> attendings=new ArrayList<>();
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        assertThrows(NotFoundException.class, ()->eventService.confirmAttendance(2L, user1));
    }
    @Test
    void confirmAttendanceThrowsBadRequestException1() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();
        Attending attending=Attending.builder()
                .id(1L)
                .event(event)
                .user(user1)
                .build();
        List<Attending> attendings=new ArrayList<>();
        attendings.add(attending);
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        assertThrows(BadRequestException.class, ()->eventService.confirmAttendance(event.getId(), user1));
    }
    @Test
    void confirmAttendanceThrowsBadRequestException2() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2024, Month.APRIL, 24, 14, 33, 48);

        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("user2_email@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();
        List<Attending> attendings=new ArrayList<>();
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        user2.setAttendings(attendings);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn(List.of(user1.getId()));
        assertThrows(BadRequestException.class, ()->eventService.confirmAttendance(event.getId(), user2));
    }
    @Test
    void notifyUsers() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(14);
        User user1 = User.builder()
                .id(1L)
                .email("tijanalazic0000@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();
        Attending attending=Attending.builder()
                .id(1L)
                .event(event)
                .user(user1)
                .build();
        List<Attending> attendings=new ArrayList<>();
        attendings.add(attending);
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(attendingRepository.findAllByEventId(event.getId())).thenReturn(List.of(attending));
        eventService.notifyUsers();
        verify(emailService,times(1)).sendEmail(anyString(),anyString(),anyString() );

    }
    @Test
    void deleteEvents(){
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(16);
        User user1 = User.builder()
                .id(1L)
                .email("tijanalazic0000@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1=GroupMember.builder()
                .id(6L)
                .dateJoined(new Date(System.currentTimeMillis()))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        Event event = Event.builder()
                .id(1L)
                .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .place("Belgrade")
                .user(user1)
                .attendings(new ArrayList<>())
                .socialGroup(socialGroup)
                .build();
        Attending attending=Attending.builder()
                .id(1L)
                .event(event)
                .user(user1)
                .build();
        List<Attending> attendings=new ArrayList<>();
        attendings.add(attending);
        event.setAttendings(attendings);
        user1.setAttendings(attendings);
        List<Event> events=new ArrayList<>();
        events.add(event);
        when(eventRepository.findAll()).thenReturn(events);
        doNothing().when(eventRepository).deleteById(event.getId());
        eventService.deleteEvents();
        verify(eventRepository,times(1)).deleteById(anyLong());

    }
}