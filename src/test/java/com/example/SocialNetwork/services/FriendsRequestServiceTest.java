package com.example.SocialNetwork.services;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.RequestStatus;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendRequestServiceImpl;
import com.example.SocialNetwork.service.FriendsService;
import com.example.SocialNetwork.service.FriendsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FriendsRequestServiceTest {

    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;
    private FriendsRepository friendsRepository;

    private FriendRequestServiceImpl friendRequestService;

    private FriendsService friendsService;

    private ModelMapper mapper;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.friendRequestRepository = mock(FriendRequestRepository.class);
        this.friendsRepository = mock(FriendsRepository.class);
        this.mapper = new ModelMapper();
        this.passwordEncoder = mock(PasswordEncoder.class);
        this.friendsService = new FriendsServiceImpl(friendsRepository, mapper, userRepository);
        this.friendRequestService = new FriendRequestServiceImpl(friendRequestRepository, userRepository, friendsService, friendsRepository, mapper);
    }

    @Test
    void sendFriendRequestSuccessfully() {
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

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId_user1(user1.getId());
        friendRequest.setId_user2(user2.getId());

        when(userRepository.getById(user1.getId())).thenReturn(user1);
        when(userRepository.getById(user2.getId())).thenReturn(user2);
        when(friendsRepository.getFriendsByUser(user1.getId())).thenReturn(Collections.emptyList());
        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);

        var result = friendRequestService.sendFriendRequest(friendRequest);
        assertNotNull(result);
    }

    @Test
    void getAllRequestsSuccessfully() {
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

        User user3 = User.builder()
                .id(3L)
                .email("user3@gmail.com")
                .username("TestUser3")
                .password(passwordEncoder.encode("user3password"))
                .active(true)
                .build();

        FriendRequest friendRequest1 = new FriendRequest();
        friendRequest1.setId_user1(user1.getId());
        friendRequest1.setId_user2(user2.getId());

        FriendRequest friendRequest2 = new FriendRequest();
        friendRequest2.setId_user1(user1.getId());
        friendRequest2.setId_user2(user3.getId());

        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(friendRequestRepository.findAllByUser2Id(user1.getId())).thenReturn(List.of(friendRequest1, friendRequest2));

        var result = friendRequestService.getAllRequests(user1.getId());
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void respondToRequestUnsuccessfully() {
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

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId_user1(user1.getId());
        friendRequest.setId_user2(user2.getId());

        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(friendRequestRepository.findById(friendRequest.getId())).thenReturn(Optional.of(friendRequest));
        when(friendsRepository.getFriendsByUser(user1.getId())).thenReturn(Collections.emptyList());

        var result = friendRequestService.respondToRequest(friendRequest.getId(), 0L);
        assertNotNull(result);
        assertEquals("User not found", result.getBody());
    }

    @Test
    void deleteFriendRequestSuccessfully() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId_user1(1L);
        friendRequest.setId_user2(2L);
        friendRequest.setStatus(RequestStatus.PENDING);

        when(friendRequestRepository.findById(any())).thenReturn(Optional.of(friendRequest));
        doNothing().when(friendRequestRepository).deletePendingById(any());

        var result = friendRequestService.deleteFriendRequest(friendRequest.getId());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Friend request deleted", result.getBody());
    }

    @Test
    void proccessRequestWithDeclineStatus() {
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

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId_user1(user1.getId());
        friendRequest.setId_user2(user2.getId());

        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);
        var result = friendRequestService.processRequest(user1, user2, 2L, friendRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Friend request declined", result.getBody());

    }

    @Test
    void proccessRequestWithAcceptStatus() {
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

        Friends friends = new Friends();
        friends.setUser1Id(user1);
        friends.setUser2Id(user2);

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId_user1(user1.getId());
        friendRequest.setId_user2(user2.getId());

        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);
        var result = friendRequestService.processRequest(user1, user2, 0L, friendRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Friend request accepted", result.getBody());

    }

}
