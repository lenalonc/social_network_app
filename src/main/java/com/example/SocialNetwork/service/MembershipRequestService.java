package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MembershipRequestService {
    List<MembershipRequest> getAllRequests();

    MembershipRequest getAllRequestsById(Long id);

    void deleteRequestById(Long id);

    void saveRequest(MembershipRequest membershipRequest);

    List<MembershipRequest> getAllRequestsForSocialGroup(Long id);

    void deleteAllRequestsForSocialGroup(Long id);

    ResponseEntity<String> createMembershipRequest(Long id, User currentUser);

    ResponseEntity<String> processJoinGroupRequest(Long groupId, User user);
}
