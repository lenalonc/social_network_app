package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.SocialGroupDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.exceptions.ValidationException;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SocialGroupServiceImpl implements SocialGroupService{
    private final ModelMapper mapper;
    private final SocialGroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MembershipRequestRepository membershipRequestRepository;

    public SocialGroupServiceImpl(SocialGroupRepository groupRepository,
                                  ModelMapper mapper,
                                  GroupMemberRepository groupMemberRepository,
                                  UserService userService,
                                  MembershipRequestRepository membershipRequestRepository,
                                  UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.mapper = mapper;
        this.groupMemberRepository = groupMemberRepository;
        this.userService = userService;
        this.membershipRequestRepository = membershipRequestRepository;
        this.userRepository = userRepository;
    }
    @Override
    public void saveGroup(SocialGroup socialGroup) {
        groupRepository.save(socialGroup);
    }

    @Override
    public List<SocialGroupDTO> getAllSocialGroups() {
        List<SocialGroup> groups = groupRepository.findAll();
        return groups.stream().map(social_group->mapper.map(social_group, SocialGroupDTO.class)).toList();
    }

    @Override
    public ResponseEntity<String> createGroup(SocialGroup group) {
        User user = userService.findCurrentUser();
        group.setUser(user);

        groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setSocialGroup(group);
        groupMember.setDateJoined(new Date());

        groupMemberRepository.save(groupMember);

        return ResponseEntity.ok("Uspesno sacuvana grupa");
    }

    public ResponseEntity<List<SocialGroupDTO>> getSocialGroupByName(String name) {
        List<SocialGroupDTO> groups = getAllSocialGroups();
        List<SocialGroupDTO> socialGroups = new ArrayList<>();

        for (SocialGroupDTO g : groups) {
            if (g.getName().equals(name)) {
                socialGroups.add(g);
            }
        }

        if (!socialGroups.isEmpty()) {
            return ResponseEntity.ok(socialGroups);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteSocialGroupById(Long id, User currentUser) {
        SocialGroup socialGroup = groupRepository.findById(id).orElse(null);

        if (socialGroup != null && currentUser.getId().equals(socialGroup.getUser().getId())) {
            membershipRequestRepository.deleteAllBySocialGroupId(id);
            groupMemberRepository.deleteAllBySocialGroupId(id);
            groupRepository.deleteByIdAndUserId(id, currentUser.getId());

            return ResponseEntity.ok("Uspesno ste obrisali grupu");
        } else {
            return ResponseEntity.status(403).body("Niste ovlasceni da obrisete grupu");
        }
    }


    @Override
    public ResponseEntity<SocialGroupDTO> getSocialGroupDTOById(Long id) {
        Optional<SocialGroup> socialGroup = groupRepository.findById(id);

        if (socialGroup.isPresent()) {
            SocialGroupDTO groupDTO = socialGroup
                    .map(socialGroupObj -> mapper.map(socialGroupObj, SocialGroupDTO.class))
                    .orElse(null);

            return ResponseEntity.ok(groupDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public SocialGroup getSocialGroupById(Long id) {
        Optional<SocialGroup> socialGroup = groupRepository.findById(id);
        return socialGroup.orElse(null);
    }

    @Override
    @Transactional
    public void changeGroupName(Long id, String name) {
        SocialGroup socialGroup = groupRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Social group does not exist"));
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));

        if(!currentUser.equals(socialGroup.getUser()))
            throw new ValidationException("Forbidden access, you are not admin");

        groupRepository.changeGroupName(id, name);
    }
}
