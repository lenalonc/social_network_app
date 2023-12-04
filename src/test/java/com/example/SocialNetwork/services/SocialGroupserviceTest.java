package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.SocialGroupDTO;
import com.example.SocialNetwork.dtos.UserCreateDto;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.SocialGroupServiceImpl;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class SocialGroupserviceTest {
    private ModelMapper mapper;
    private SocialGroupRepository groupRepository;
    private GroupMemberRepository groupMemberRepository;
    private UserRepository userRepository;
    private MembershipRequestRepository membershipRequestRepository;
    private SocialGroupServiceImpl socialGroupService;

    @BeforeEach
    void setUp(){
        this.mapper = new ModelMapper();
        this.userRepository = mock(UserRepository.class);
        this.groupRepository = mock(SocialGroupRepository.class);
        this.membershipRequestRepository = mock(MembershipRequestRepository.class);
        this.groupMemberRepository = mock(GroupMemberRepository.class);
        this.socialGroupService = new SocialGroupServiceImpl(groupRepository, mapper,
                groupMemberRepository, membershipRequestRepository, userRepository);
    }

    @Test
    void getAllSocialGroupsSuccessfully() {
        SocialGroup socialGroup = SocialGroup.builder()
                .user(new User())
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();

        SocialGroup socialGroup2 = SocialGroup.builder()
                .user(new User())
                .id(2L)
                .name("grupa2")
                .type(true)
                .build();


        when(groupRepository.findAll()).thenReturn(List.of(socialGroup, socialGroup2));

        var result = socialGroupService.getAllSocialGroups();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteSocialGroupsByIdSuccessfully(){
        User user = new User();
        user.setId(1L);

        SocialGroup socialGroup = SocialGroup.builder()
                .user(user)
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();

        when(groupRepository.findById(1L)).thenReturn(Optional.of(socialGroup));
        assertDoesNotThrow(() -> socialGroupService.deleteSocialGroupById(1L, user),
                "Social group not found");
    }

    @Test
    void getSocialGroupByIdNotFoundException(){
        SocialGroup socialGroup = SocialGroup.builder()
                .user(new User())
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        var result = groupRepository.findById(1L);
        assertThrows(NotFoundException.class,() -> socialGroupService.getSocialGroupById(1L));
    }

    @Test
    void getSocialGroupByNameSuccessfully(){
        SocialGroup socialGroup = SocialGroup.builder()
                .user(new User())
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();

        SocialGroup socialGroup2 = SocialGroup.builder()
                .user(new User())
                .id(2L)
                .name("grupa2")
                .type(true)
                .build();


        when(groupRepository.findAll()).thenReturn(List.of(socialGroup, socialGroup2));

        var res = socialGroupService.getSocialGroupByName("grupa1");

        assertNotNull(res);
        assertEquals(res.get(0).getName(), socialGroup.getName());
    }

    @Test
    void createGroupSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user");

        SocialGroup socialGroup = SocialGroup.builder()
                .user(user)
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();

        GroupMember groupMember = GroupMember.builder()
                .user(user)
                .socialGroup(socialGroup)
                .dateJoined(new Date()).build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(groupRepository.save(socialGroup)).thenReturn(socialGroup);
        var result = socialGroupService.createGroup(socialGroup);

        assertNotNull(result);
        assertEquals(socialGroup.getId(), result.getId());

        assertDoesNotThrow(() -> groupMemberRepository.save(groupMember),
                "Error");
    }

    @Test
    void changeGroupNameSuccessfully(){
        User user = new User();
        user.setId(1L);
        user.setEmail("user");

        SocialGroup socialGroup = SocialGroup.builder()
                .user(user)
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();

        var authenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(socialGroup));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> socialGroupService.changeGroupName(1L, "grupica"),
                "Error");
    }

    @Test
    void getSocialGroupDTOById(){
        SocialGroup socialGroup = SocialGroup.builder()
                .user(new User())
                .id(1L)
                .name("grupa1")
                .type(false)
                .build();

        when(groupRepository.findById(1L)).thenReturn(Optional.of(socialGroup));

        var result = socialGroupService.getSocialGroupDTOById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

}
