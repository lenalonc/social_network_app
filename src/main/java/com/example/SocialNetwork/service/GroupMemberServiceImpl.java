package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.GroupMemberDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.exceptions.ValidationException;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberServiceImpl implements GroupMemberService{

    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final SocialGroupRepository socialGroupRepository;
    private final MembershipRequestRepository membershipRequestRepository;
    private final ModelMapper mapper;

    public GroupMemberServiceImpl(GroupMemberRepository memberRepository,
                                  UserRepository userRepository,
                                  SocialGroupRepository socialGroupRepository,
                                  MembershipRequestRepository membershipRequestRepository,
                                  ModelMapper mapper){
        this.groupMemberRepository = memberRepository;
        this.userRepository = userRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.membershipRequestRepository = membershipRequestRepository;
        this.mapper = mapper;
    }
    @Override
    public GroupMemberDTO saveGroupMember(Long id) {

        MembershipRequest membershipRequest = membershipRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Membership request not found"));

        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));

        SocialGroup socialGroup = membershipRequest.getSocialGroup();

        if(currentUser.getId() == socialGroup.getUser().getId()){
            Date date = new Date();
            GroupMember groupMember = new GroupMember();
            groupMember.setDateJoined(date);
            groupMember.setUser(membershipRequest.getUser());
            groupMember.setSocialGroup(socialGroup);

            GroupMemberDTO groupMemberDTO = this.mapper.map(groupMemberRepository.save(groupMember),
                    GroupMemberDTO.class);

            membershipRequestRepository.deleteById(membershipRequest.getId());

            return groupMemberDTO;
        }

        throw new ForbiddenException("You are not allowed for this action");
    }
    @Override
    public List<UserDTO> getAllGroupMembers(Long id) {
        List<UserDTO> users = new ArrayList<>();
        List<Long> groupMemberId = groupMemberRepository.findAllM(id);

        if(groupMemberId.isEmpty())
            throw new NotFoundException("No members found in group");

        for (Long aLong: groupMemberId) {
            users.add(mapper.map(userRepository.findById(aLong).get(), UserDTO.class));
        }
        return users;
    }

    @Override
    @Transactional
    public void removeCurrentUserFromGroupByGroupId(Long id) {
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));
        SocialGroup socialGroup = socialGroupRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Social group not found"));

        if(currentUser.getId() == socialGroup.getUser().getId()){
            throw new ForbiddenException("You are admin, cannot leave a group you created");
        }
        groupMemberRepository.deleteByUserId(currentUser.getId(), id);
    }

    @Override
    @Transactional
    public void deleteAllGroupMembers(Long groupId) {
        List<UserDTO> groupMemberIds = getAllGroupMembers(groupId);
        for (UserDTO memberId : groupMemberIds) {
            removeCurrentUserFromGroupByGroupId(memberId.getId());
        }
    }

    @Override
    @Transactional
    public void removeUserFromGroupByUserID(Long userId, Long groupId) {
        User userForRemoval = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found"));
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).orElseThrow(() ->
                new NotFoundException("Social group not found"));

        if(socialGroup.getUser().equals(currentUser) && currentUser.
                equals(userForRemoval))
            throw new ForbiddenException("You are admin of this group, cannot execute your operation");


        groupMemberRepository.deleteByUserId(userId,groupId);
    }


}
