package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.MembershipRequestDTO;
import com.example.SocialNetwork.entities.*;
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

@Service
public class MembershipRequestServiceImpl implements MembershipRequestService {

    private final MembershipRequestRepository membershipRequestsRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SocialGroupRepository socialGroupRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public MembershipRequestServiceImpl(MembershipRequestRepository requestsRepository,
                                        GroupMemberRepository groupMemberRepository,
                                        SocialGroupRepository socialGroupRepository,
                                        UserRepository userRepository, ModelMapper mapper){
        this.membershipRequestsRepository = requestsRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    @Override
    public List<MembershipRequestDTO> getAllRequests() {
        List<MembershipRequest> requests = membershipRequestsRepository.findAll();
        return requests.stream().map(membership_request->mapper.map(membership_request, MembershipRequestDTO.class)).toList();
}

    @Override
    public MembershipRequestDTO getRequestsById(Long id) {
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));
        MembershipRequest membershipRequest = membershipRequestsRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Membership requests not found"));

        if(membershipRequest.getUser().getId().equals(currentUser.getId())
                || membershipRequest.getSocialGroup().getUser().getId().equals(currentUser.getId())){
            return this.mapper.map(membershipRequestsRepository.save(membershipRequest),MembershipRequestDTO.class);
        }
        else
            throw new ValidationException("Forbidden access.");
    }

    @Override
    @Transactional
    public void deleteRequestById(Long id) {
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));

        MembershipRequestDTO membershipRequest = getRequestsById(id);

        if(membershipRequest.getUser().getId() == currentUser.getId() ||
            membershipRequest.getSocialGroup().getUser().getId() == currentUser.getId()){
            membershipRequestsRepository.deleteById(id);
        }
    }

    @Override
    public List<MembershipRequestDTO> getAllRequestsForSocialGroup(Long id) {
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));

        SocialGroup socialGroup = socialGroupRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Social group not found"));

        if(socialGroup.getUser().getId() == currentUser.getId()){
            List<MembershipRequest> membershipRequests = membershipRequestsRepository.
                    findAllMembershipRequestsForSocialGroup(id);
            List<MembershipRequestDTO> membershipRequestDTO = new ArrayList<>();
            for(MembershipRequest membershipRequest: membershipRequests){
                membershipRequestDTO.add(mapper.map(membershipRequestsRepository.
                        findById(membershipRequest.getId()).get(),MembershipRequestDTO.class));
            }
            return  membershipRequestDTO;
        }
        else
            throw new ForbiddenException("You are not authorized");
    }

    @Override
    @Transactional
    public void deleteAllRequestsForSocialGroup(Long groupId) {
        List<MembershipRequestDTO> requests = getAllRequestsForSocialGroup(groupId);
        for (MembershipRequestDTO request : requests) {
            deleteRequestById(request.getId());
        }
    }

    @Override
    public MembershipRequestDTO createMembershipRequest(Long groupId) {
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).orElseThrow(() ->
                new NotFoundException("Social group not found"));
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));

        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setUser(currentUser);
        membershipRequest.setSocialGroup(socialGroup);
        membershipRequest.setRequestStatus(RequestStatus.PENDING);
        membershipRequestsRepository.save(membershipRequest);

        return this.mapper.map(membershipRequestsRepository.save(membershipRequest),MembershipRequestDTO.class);
    }

    @Override
    public void processJoinGroupRequest(Long groupId) {
        User currentUser = userRepository.findByEmail(SecurityContextHolder.
                getContext().getAuthentication().getName()).orElseThrow(() ->
                new NotFoundException("User not found"));
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).orElseThrow(() ->
                new NotFoundException("Social group not found"));

        if (socialGroup.isType()) {
            List<MembershipRequest> membershiprequests = socialGroup.getMembershipRequest();
            for(MembershipRequest request:membershiprequests){
                if(request.getUser()==currentUser){
                    throw new ForbiddenException("The request already exists");
                }
            }
             createMembershipRequest(groupId);

        } else {
            List<User> groupUsers= socialGroup.getUsers();
            for(User groupUser: groupUsers)
                if(groupUser==currentUser){
                    throw new ForbiddenException("You are already a member of the group");
                }
            GroupMember groupMember = new GroupMember();
            groupMember.setUser(currentUser);
            groupMember.setSocialGroup(socialGroup);
            groupMember.setDateJoined(new Date());
            groupMemberRepository.save(groupMember);
        }
    }

}
