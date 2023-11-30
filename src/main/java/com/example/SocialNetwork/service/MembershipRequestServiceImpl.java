package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipRequestServiceImpl implements MembershipRequestService {

    private final MembershipRequestRepository requestsRepository;

    public MembershipRequestServiceImpl(MembershipRequestRepository requestsRepository){
        this.requestsRepository = requestsRepository;
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
}
