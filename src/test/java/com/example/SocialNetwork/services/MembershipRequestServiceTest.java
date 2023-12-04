package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.MembershipRequestDTO;
import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.MembershipRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MembershipRequestServiceTest {
    private MembershipRequestRepository membershipRequestRepository;
    private SocialGroupRepository socialGroupRepository;
    private ModelMapper mapper;
    private GroupMemberRepository groupMemberRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private MembershipRequestServiceImpl membershipRequestService;

    @BeforeEach
    void setUp(){
        this.membershipRequestRepository = mock(MembershipRequestRepository.class);
        this.socialGroupRepository = mock(SocialGroupRepository.class);
        this.groupMemberRepository = mock(GroupMemberRepository.class);
        this.mapper = new ModelMapper();
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = mock(UserRepository.class);
        this.membershipRequestService = new MembershipRequestServiceImpl(membershipRequestRepository, groupMemberRepository, socialGroupRepository, userRepository, mapper);
    }

    @Test
    void getAllRequests_Successful() {
        MembershipRequest membershipRequest1 = MembershipRequest.builder()
                .id(1L)
                .user(new User())
                .socialGroup(new SocialGroup())
                .requestStatus(RequestStatus.PENDING)
                .build();

        MembershipRequest membershipRequest2 = MembershipRequest.builder()
                .id(2L)
                .user(new User())
                .socialGroup(new SocialGroup())
                .requestStatus(RequestStatus.ACCEPTED)
                .build();

        when(membershipRequestRepository.findAll()).thenReturn(List.of(membershipRequest1, membershipRequest2));

        List<MembershipRequestDTO> result = membershipRequestService.getAllRequests();

        assertEquals(2, result.size());
    }

    @Test
    void getAllRequests_EmptyList() {
        when(membershipRequestRepository.findAll()).thenReturn(Collections.emptyList());
        List<MembershipRequestDTO> result = membershipRequestService.getAllRequests();
        assertTrue(result.isEmpty());
    }


    @Test
    void getRequestsById_Successful() {
        User user1 = User.builder()
                .id(1L)
                .email("user1_email@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();
        var autheticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(autheticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .user(user1)
                .socialGroup(new SocialGroup())
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(membershipRequestRepository.findById(1L)).thenReturn(Optional.of(membershipRequest));
        when(membershipRequestRepository.save(any(MembershipRequest.class))).thenReturn(membershipRequest);
        var result = membershipRequestService.getRequestsById(1L);
        assertNotNull(result);
    }

    @Test
    void getRequestsById_UserNotLoggedIn() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.getRequestsById(1L));

        assertEquals("User not found", exception.getMessage());
    }
    @Test
    void getRequestsById_MembershipRequestNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        when(membershipRequestRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.getRequestsById(1L));

        assertEquals("Membership requests not found", exception.getMessage());
    }

    @Test
    void getAllRequestsForSocialGroup_UserNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.getAllRequestsForSocialGroup(1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllRequestsForSocialGroup_SocialGroupNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        when(socialGroupRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.getAllRequestsForSocialGroup(1L));

        assertEquals("Social group not found", exception.getMessage());
    }

    @Test
    void deleteRequestById_UserNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.deleteRequestById(1L));

        assertEquals("User not found", exception.getMessage());
        verify(membershipRequestRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteRequestById_Successful() {
        // Mock authenticated user
        User currentUser = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .username("TestUser")
                .password(passwordEncoder.encode("testpassword"))
                .active(true)
                .build();
        var authenticationToken = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(currentUser));

        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .user(currentUser)
                .socialGroup(new SocialGroup())
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(membershipRequestRepository.findById(1L)).thenReturn(Optional.of(membershipRequest));
        when(membershipRequestRepository.save(any(MembershipRequest.class))).thenReturn(membershipRequest);

        membershipRequestService.deleteRequestById(1L);

        verify(membershipRequestRepository).deleteById(1L);
    }

    @Test
    void getAllRequestsForSocialGroup_Successful(){
        User currentUser = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .username("TestUser")
                .password(passwordEncoder.encode("testpassword"))
                .active(true)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("grupa1")
                .type(false)
                .user(currentUser).build();

        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .user(currentUser)
                .socialGroup(socialGroup)
                .requestStatus(RequestStatus.PENDING)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(currentUser));
        when(socialGroupRepository.findById(1L)).thenReturn(Optional.of(socialGroup));


        when(membershipRequestRepository.findAllMembershipRequestsForSocialGroup(1L)).
                thenReturn(List.of(membershipRequest));

        when(membershipRequestRepository.findById(1L)).thenReturn(Optional.of(membershipRequest));


        var result = membershipRequestService.getAllRequestsForSocialGroup(1L);

        assertEquals(1 ,result.size());
    }

    @Test
    void processJoinGroupRequest_Successful() {
        User currentUser = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .username("TestUser")
                .password(passwordEncoder.encode("testpassword"))
                .active(true)
                .build();

        User user111 = User.builder()
                .id(2L)
                .email("test2test")
                .username("ttt")
                .password(passwordEncoder.encode("testpassword"))
                .active(true)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user111);

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("grupa1")
                .type(false)
                .user(currentUser)
                .users(users)
                .build();

        GroupMember groupMember = GroupMember.builder()
                .id(1L)
                .user(currentUser)
                .socialGroup(socialGroup)
                .dateJoined(new Date())
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(currentUser));
        when(socialGroupRepository.findById(1L)).thenReturn(Optional.of(socialGroup));
        when(groupMemberRepository.save(any(GroupMember.class))).thenReturn(groupMember);

        assertDoesNotThrow(() -> membershipRequestService.processJoinGroupRequest(1L),
                "Error");

        //verify(membershipRequestService, times(1)).createMembershipRequest(1L);
    }

    @Test
    void processJoinGroupRequest_UserNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.processJoinGroupRequest(1L));

        assertEquals("User not found", exception.getMessage());
        verify(membershipRequestRepository, never()).deleteById(anyLong());
    }
    @Test
    void processJoinGroupRequest_SocialGroupNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        when(socialGroupRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.processJoinGroupRequest(1L));

        assertEquals("Social group not found", exception.getMessage());
    }

    @Test
    void createMembershipRequest_Successful() {
        User currentUser = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .username("TestUser")
                .password(passwordEncoder.encode("testpassword"))
                .active(true)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("grupa1")
                .type(false)
                .user(currentUser)
                .users(Collections.singletonList(currentUser))
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(currentUser));
        when(socialGroupRepository.findById(1L)).thenReturn(Optional.of(socialGroup));

        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .user(currentUser)
                .socialGroup(socialGroup)
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(membershipRequestRepository.save(any(MembershipRequest.class))).thenReturn(membershipRequest);

        MembershipRequestDTO result = membershipRequestService.createMembershipRequest(1L);

        assertNotNull(result);
        assertEquals(currentUser.getId(), result.getUser().getId());
        assertEquals(socialGroup.getId(), result.getSocialGroup().getId());
        assertEquals(RequestStatus.PENDING, result.getRequestStatus());
    }

    @Test
    void createMembershipRequest_SocialGroupNotFound() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(socialGroupRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> membershipRequestService.createMembershipRequest(1L));
        assertEquals("Social group not found", exception.getMessage());
    }


}
