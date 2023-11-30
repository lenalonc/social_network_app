package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.MembershipRequest;

import java.util.List;

public interface MembershipRequestService {
    List<MembershipRequest> getAllRequests();

    MembershipRequest getAllRequestsById(Long id);

    void deleteRequestById(Long id);

    void saveRequest(MembershipRequest membershipRequest);

    List<MembershipRequest> getAllRequestsForSocialGroup(Long id);

    void deleteAllRequestsForSocialGroup(Long id);
}
