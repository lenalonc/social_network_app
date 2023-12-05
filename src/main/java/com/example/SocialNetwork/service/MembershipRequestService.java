package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.MembershipRequestDTO;

import java.util.List;

public interface MembershipRequestService {
    List<MembershipRequestDTO> getAllRequests();

    MembershipRequestDTO getRequestsById(Long id);

    void deleteRequestById(Long id);

    List<MembershipRequestDTO> getAllRequestsForSocialGroup(Long id);

    void deleteAllRequestsForSocialGroup(Long id);

    MembershipRequestDTO createMembershipRequest(Long id);

    void processJoinGroupRequest(Long groupId);
}
