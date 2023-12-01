package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipRequestServiceImpl implements MembershipRequestService {

    private final MembershipRequestRepository membershipRequestsRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SocialGroupRepository socialGroupRepository;
    private final UserRepository userRepository;

    public MembershipRequestServiceImpl(MembershipRequestRepository requestsRepository,
                                        GroupMemberRepository groupMemberRepository,
                                        SocialGroupRepository socialGroupRepository,
                                        UserRepository userRepository){
        this.membershipRequestsRepository = requestsRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<MembershipRequest> getAllRequests() {
        return membershipRequestsRepository.findAll();
    }

    @Override
    public MembershipRequest getRequestsById(Long id) {
        Optional<User> currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<MembershipRequest> membershipRequest = membershipRequestsRepository.findById(id);

        if(membershipRequest.orElse(null).getUser().getId() == currentUser.get().getId()
            || membershipRequest.orElse(null).getSocialGroup().getUser().getId()
                == currentUser.get().getId()){
            return membershipRequest.get();
        }
        return null;
    }

    @Override
    public void deleteRequestById(Long id) {
        Optional<User> currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        MembershipRequest membershipRequest = getRequestsById(id);

        if(membershipRequest.getUser().getId() == currentUser.get().getId() ||
            membershipRequest.getSocialGroup().getUser().getId() == currentUser.get().getId()){
            membershipRequestsRepository.deleteById(id);
        }
    }

    @Override
    public void saveRequest(MembershipRequest membershipRequest) {
        membershipRequestsRepository.save(membershipRequest);
    }

    @Override
    public List<MembershipRequest> getAllRequestsForSocialGroup(Long id) {
        Optional<User> currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<SocialGroup> socialGroup = socialGroupRepository.findById(id);

        if(socialGroup.get().getUser().getId() == currentUser.get().getId()){
            return membershipRequestsRepository.findAllMembershipRequestsForSocialGroup(id);
        }
        return null;
    }

    @Override
    public void deleteAllRequestsForSocialGroup(Long groupId) {
        List<MembershipRequest> requests = getAllRequestsForSocialGroup(groupId);
        for (MembershipRequest request : requests) {
            deleteRequestById(request.getId());
        }
    }

    @Override
    public ResponseEntity<String> createMembershipRequest(Long groupId, User user) {
        try {
            Optional<SocialGroup> socialGroupOptional = socialGroupRepository.findById(groupId);

            if (socialGroupOptional.isPresent()) {
                SocialGroup socialGroup = socialGroupOptional.get();

                if (user != null) {
                    MembershipRequest membershipRequest = new MembershipRequest();
                    membershipRequest.setSocialGroup(socialGroup);
                    membershipRequest.setUser(user);
                    membershipRequest.setRequestStatus(RequestStatus.PENDING);
                    membershipRequestsRepository.save(membershipRequest);

                    return ResponseEntity.ok("Uspešno ste poslali request za učlanjenje u grupu");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Zahteve mogu poslati samo ulogovani korisnici");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Grupa sa ID " + groupId + " nije pronađena");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Došlo je do greške prilikom slanja zahteva za učlanjenje u grupu.");
        }
    }

    @Override
    public ResponseEntity<String> processJoinGroupRequest(Long groupId, User user) {
        try {
            Optional<SocialGroup> socialGroupOptional = socialGroupRepository.findById(groupId);

            if (socialGroupOptional.isPresent()) {
                SocialGroup socialGroup = socialGroupOptional.get();
                if (socialGroup.isType()==true) {
                    return createMembershipRequest(groupId, user);
                } else {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setUser(user);
                    groupMember.setSocialGroup(socialGroup);
                    groupMember.setDateJoined(new Date());
                    groupMemberRepository.save(groupMember);

                    return ResponseEntity.ok("Uspešno ste se pridružili grupi");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupa nije pronađena.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Došlo je do greške prilikom pridruživanja grupe.");
        }
    }



}
