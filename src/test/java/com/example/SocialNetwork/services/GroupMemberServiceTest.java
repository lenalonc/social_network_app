package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.GroupMemberDTO;
import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.GroupMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class GroupMemberServiceTest {

    private GroupMemberRepository groupMemberRepository;
    private UserRepository userRepository;
    private SocialGroupRepository socialGroupRepository;
    private MembershipRequestRepository membershipRequestRepository;
    private ModelMapper modelMapper;
    private GroupMemberService groupMemberService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.groupMemberRepository = mock(GroupMemberRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.socialGroupRepository = mock(SocialGroupRepository.class);
        this.membershipRequestRepository = mock(MembershipRequestRepository.class);
        this.modelMapper = new ModelMapper();
        this.groupMemberService = new GroupMemberServiceImpl(groupMemberRepository, userRepository, socialGroupRepository, membershipRequestRepository, modelMapper);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void saveGroupMember() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .requestStatus(RequestStatus.PENDING)
                .socialGroup(socialGroup)
                .user(user1)
                .build();
        GroupMember groupMember = GroupMember.builder()
                .id(1L)
                .dateJoined(new Date(2023, Calendar.JANUARY, 12))
                .user(user1)
                .socialGroup(socialGroup)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(membershipRequestRepository.findById(anyLong())).thenReturn(Optional.of(membershipRequest));

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(groupMemberRepository.save(any())).thenReturn(groupMember);

        assertDoesNotThrow(() -> membershipRequestRepository.deleteById(membershipRequest.getId()));

        var result = groupMemberService.saveGroupMember(groupMember.getId());

        assertNotNull(result);
        assertEquals(groupMember.getId(), result.getId());
    }

    @Test
    void saveGroupMemberThrowsMembershipNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .doNotDisturb(null)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .requestStatus(RequestStatus.PENDING)
                .socialGroup(socialGroup)
                .user(user1)
                .build();
        when(membershipRequestRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.saveGroupMember(socialGroup.getId()));
    }

    @Test
    void saveGroupMemberThrowsUserNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .doNotDisturb(null)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .requestStatus(RequestStatus.PENDING)
                .socialGroup(socialGroup)
                .user(user1)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(membershipRequestRepository.findById(any())).thenReturn(Optional.ofNullable(membershipRequest));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.saveGroupMember(socialGroup.getId()));
    }

    @Test
    void saveGroupMemberThrowsForbiddenException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .doNotDisturb(null)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(new User())
                .type(true)
                .build();
        MembershipRequest membershipRequest = MembershipRequest.builder()
                .id(1L)
                .requestStatus(RequestStatus.PENDING)
                .socialGroup(socialGroup)
                .user(user1)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(membershipRequestRepository.findById(any())).thenReturn(Optional.ofNullable(membershipRequest));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user1));

        assertThrows(ForbiddenException.class, () -> groupMemberService.saveGroupMember(socialGroup.getId()));
    }

    @Test
    void getAllGroupMembersSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("user")
                .username("user")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1 = GroupMember.builder()
                .id(1L)
                .dateJoined(new Date(2022, 12, 12))
                .user(user1)
                .socialGroup(socialGroup)
                .build();
        GroupMember groupMember2 = GroupMember.builder()
                .id(2L)
                .dateJoined(new Date(2022, 11, 11))
                .user(user2)
                .socialGroup(socialGroup)
                .build();

        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn((List.of(user1.getId(), user2.getId())));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        var result = groupMemberService.getAllGroupMembers(socialGroup.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllGroupMembersThrowsNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("user")
                .username("user")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();
        GroupMember groupMember1 = GroupMember.builder()
                .id(1L)
                .dateJoined(new Date(2022, 12, 12))
                .user(user1)
                .socialGroup(socialGroup)
                .build();
        GroupMember groupMember2 = GroupMember.builder()
                .id(2L)
                .dateJoined(new Date(2022, 11, 11))
                .user(user2)
                .socialGroup(socialGroup)
                .build();
        List<GroupMember> groupMembers = List.of(groupMember1, groupMember2);
        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn(new ArrayList<>());
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        assertThrows(NotFoundException.class, () -> groupMemberService.getAllGroupMembers(socialGroup.getId()));

        verify(groupMemberRepository, times(1)).findAllM(anyLong());
        verifyNoMoreInteractions(groupMemberRepository);
    }

    @Test
    void removeCurrentUserFromGroupByGroupIdSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .doNotDisturb(null)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(new User())
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(socialGroupRepository.findById(socialGroup.getId())).thenReturn(Optional.of(socialGroup));

        assertDoesNotThrow(() -> groupMemberRepository.deleteById(anyLong()));

        groupMemberService.removeCurrentUserFromGroupByGroupId(socialGroup.getId());
    }

    @Test
    void removeCurrentUserFromGroupByGroupIdThrowsUserNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .doNotDisturb(null)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.removeCurrentUserFromGroupByGroupId(anyLong()));
    }

    @Test
    void removeCurrentUserFromGroupByGroupIdThrowsSocialGroupNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .doNotDisturb(null)
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user1));
        when(socialGroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.removeCurrentUserFromGroupByGroupId(anyLong()));
    }

    @Test
    void deleteAllGroupMembers() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .username("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("userr")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(groupMemberRepository.findAllM(socialGroup.getId())).thenReturn((List.of(user1.getId(), user2.getId())));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user2));

        when(socialGroupRepository.findById(any())).thenReturn(Optional.of(socialGroup));

        assertDoesNotThrow(() -> groupMemberRepository.deleteById(anyLong()));
        assertDoesNotThrow(() -> groupMemberRepository.deleteById(anyLong()));

        groupMemberService.deleteAllGroupMembers(socialGroup.getId());
    }

    @Test
    void removeUserFromGroupByUserID() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("userr")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user2)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(socialGroupRepository.findById(any())).thenReturn(Optional.of(socialGroup));

        assertDoesNotThrow(() -> groupMemberRepository.deleteById(anyLong()));

        groupMemberService.removeUserFromGroupByUserID(user1.getId(), socialGroup.getId());
    }

    @Test
    void removeUserFromGroupByUserIDThrowsUserNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("userr")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user2)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.removeUserFromGroupByUserID(user1.getId(), socialGroup.getId()));
    }

    @Test
    void removeUserFromGroupByUserIDThrowsUserNotFoundException2() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("userr")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user2)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.removeUserFromGroupByUserID(user1.getId(), socialGroup.getId()));
    }

    @Test
    void removeUserFromGroupByUserIDThrowsSocialGroupNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("userr")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user2)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(socialGroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupMemberService.removeUserFromGroupByUserID(user1.getId(), socialGroup.getId()));
    }

    @Test
    void removeUserFromGroupByUserIDThrowsForbiddenException() {
        User user1 = User.builder()
                .id(1L)
                .email("user")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("userr")
                .build();
        SocialGroup socialGroup = SocialGroup.builder()
                .id(2L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(socialGroupRepository.findById(any())).thenReturn(Optional.of(socialGroup));

        assertThrows(ForbiddenException.class, () -> groupMemberService.removeUserFromGroupByUserID(user1.getId(), socialGroup.getId()));
    }
}