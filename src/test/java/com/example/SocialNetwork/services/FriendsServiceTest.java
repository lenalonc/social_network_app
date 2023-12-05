package com.example.SocialNetwork.services;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FriendsServiceTest {

    private ModelMapper mapper;
    private FriendsRepository friendsRepository;
    private UserRepository userRepository;

    private FriendsServiceImpl friendsService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.friendsRepository = mock(FriendsRepository.class);
        this.userRepository=mock(UserRepository.class);
        this.mapper=new ModelMapper();
        this.passwordEncoder=new BCryptPasswordEncoder();
        this.friendsService=new FriendsServiceImpl(friendsRepository, mapper, userRepository);
    }

    @Test
    void saveFriendsSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();

        Friends friends = new Friends(1L, user1, user2);

        when(friendsRepository.save(friends)).thenReturn(friends);

        friendsService.saveFriends(friends);

        assertNotNull(friends);
    }

    @Test
    void getFriendsByUserSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();

        Friends friends = new Friends(1L, user1, user2);

        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(friendsRepository.getFriendsByUser(user1.getId())).thenReturn(List.of(user2));

        var result = friendsService.getFriendsByUser();

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    void deleteFriendUnsuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();

        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(friendsRepository.getFriendsByUser(user1.getId())).thenReturn(Collections.emptyList());

        var result = friendsService.deleteFriend(2L);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Friend not found", result.getBody());
    }

    @Test
    void searchFriendsReturnsNotFound() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();

        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(friendsRepository.searchFriends(user1.getId(), "TestUser2")).thenReturn(Collections.emptyList());

        var result = friendsService.searchFriends("TestUser2");

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void searchFriendsReturnsOk() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();

        Friends friends = new Friends(1L, user1, user2);

        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(friendsRepository.searchFriends(user1.getId(), user2.getUsername())).thenReturn(List.of(user2));

        var result = friendsService.searchFriends(user2.getUsername());
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}
