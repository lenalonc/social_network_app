package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
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
        else
            throw new NotFoundException("Membership request not found");
    }

    @Override
    @Transactional
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
        else
            throw new NotFoundException("There is no membership requests found");
    }

    @Override
    @Transactional
    public void deleteAllRequestsForSocialGroup(Long groupId) {
        List<MembershipRequest> requests = getAllRequestsForSocialGroup(groupId);
        for (MembershipRequest request : requests) {
            deleteRequestById(request.getId());
        }
    }

    @Override
    public ResponseEntity<String> createMembershipRequest(Long groupId, User user) {
        try {
            Optional<SocialGroup> socialGroup = socialGroupRepository.findById(groupId);

            if (socialGroup.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupa nije pronadjena");
            }
            else{
                    if (user != null) {
                        MembershipRequest membershipRequest = new MembershipRequest();
                        membershipRequest.setSocialGroup(socialGroup.get());
                        membershipRequest.setUser(user);
                        membershipRequest.setRequestStatus(RequestStatus.PENDING);
                        membershipRequestsRepository.save(membershipRequest);

                        return ResponseEntity.ok("Uspešno ste poslali request za učlanjenje u grupu");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Zahteve mogu poslati samo ulogovani korisnici");
                    }
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
                if (socialGroup.isType()) {
                    List<MembershipRequest> membershiprequests = socialGroup.getMembershipRequest();
                    for(MembershipRequest request:membershiprequests){
                        if(request.getUser()==user){
                            return ResponseEntity.ok("Vec ste poslali zahtev");
                        }
                    }
                    return createMembershipRequest(groupId, user);
                } else {
                    List<User> groupUsers= socialGroup.getUsers();
                    for(User groupUser: groupUsers){
                        if(groupUser==user){
                            return ResponseEntity.ok("Vec ste clan grupe");
                        }

                    }
                    GroupMember groupMember = new GroupMember();
                    groupMember.setUser(user);
                    groupMember.setSocialGroup(socialGroup);
                    groupMember.setDateJoined(new Date());
                    groupMemberRepository.save(groupMember);

                    return ResponseEntity.ok("Uspesno ste se pridruzili grupi");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupa nije pronadjena.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Doslo je do greske prilikom pridruzivanja grupe.");
        }
    }



}
