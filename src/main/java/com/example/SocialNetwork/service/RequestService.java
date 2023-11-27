package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;

import java.util.List;

public interface RequestService {
    List<MembershipRequest> getAllRequests();

    MembershipRequest getAllRequestsById(Long id);

    void deleteRequestById(Long id);

    void saveRequest(MembershipRequest membershipRequest);
}
