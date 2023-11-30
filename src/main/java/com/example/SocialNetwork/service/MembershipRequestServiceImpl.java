package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipRequestServiceImpl implements MembershipRequestService {

    private final MembershipRequestRepository requestsRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SocialGroupRepository socialGroupRepository;

    public MembershipRequestServiceImpl(MembershipRequestRepository requestsRepository, GroupMemberRepository groupMemberRepository, SocialGroupRepository socialGroupRepository){
        this.requestsRepository = requestsRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.socialGroupRepository = socialGroupRepository;
    }
    @Override
    public List<MembershipRequest> getAllRequests() {
        return requestsRepository.findAll();
    }

    @Override
    public MembershipRequest getAllRequestsById(Long id) {
        Optional<MembershipRequest> request = requestsRepository.findById(id);
        return request.orElse(null);
    }

    @Override
    public void deleteRequestById(Long id) {
        MembershipRequest temGroup = requestsRepository.findById(id).get();
        requestsRepository.deleteById(id);
    }

    @Override
    public void saveRequest(MembershipRequest membershipRequest) {
        requestsRepository.save(membershipRequest);
    }

    @Override
    public List<MembershipRequest> getAllRequestsForSocialGroup(Long id) {
        return requestsRepository.findAllMembershipRequestsForSocialGroup(id);
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
                    requestsRepository.save(membershipRequest);

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
