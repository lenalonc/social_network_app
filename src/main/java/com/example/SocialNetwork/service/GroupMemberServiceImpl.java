package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.MembershipRequest;
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
    public GroupMember saveGroupMember(Long id) {

        Optional<MembershipRequest> membershipRequest = membershipRequestRepository.findById(id);

        if(membershipRequest.orElse(null) == null)
            throw new NotFoundException("Membership request not found");

        Optional<User> currentUser = userRepository.
                findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SocialGroup socialGroup = membershipRequest.get().getSocialGroup();

        if(currentUser.get().getId() == socialGroup.getUser().getId()){
            Date date = new Date();
            GroupMember groupMember = new GroupMember();
            groupMember.setDateJoined(date);
            groupMember.setUser(membershipRequest.get().getUser());
            groupMember.setSocialGroup(socialGroup);

            groupMemberRepository.save(groupMember);
            Optional<MembershipRequest> request = membershipRequestRepository.findById(id);
            membershipRequestRepository.deleteById(request.get().getId());

            return groupMember;
        }

        return null;
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
        Optional<User> currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<SocialGroup> socialGroup = socialGroupRepository.findById(id);

        if(currentUser.get().getId() != socialGroup.get().getUser().getId()){
            groupMemberRepository.deleteByUserId(currentUser.get().getId(), id);
        }
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
        Optional<User> userForRemoval = userRepository.findById(userId);
        Optional<User> currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(userForRemoval.isEmpty())
            throw new NotFoundException("User not found");

        if(currentUser.get().getId() == userForRemoval.get().getId())
            throw new ValidationException("You are admin of this group, cannot execute your operation");

        groupMemberRepository.deleteByUserId(userId,groupId);
    }


}
