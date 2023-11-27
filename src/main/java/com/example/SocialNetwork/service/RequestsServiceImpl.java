package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.repository.ReqestsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestsServiceImpl implements RequestService{

    private ReqestsRepository reqestsRepository;

    public RequestsServiceImpl(ReqestsRepository reqestsRepository){
        this.reqestsRepository = reqestsRepository;
    }
    @Override
    public List<MembershipRequest> getAllRequests() {
        return reqestsRepository.findAll();
    }

    @Override
    public MembershipRequest getAllRequestsById(Long id) {
        Optional<MembershipRequest> request = reqestsRepository.findById(id);
        if(request.isPresent()){
            return request.get();
        }
        return null;
    }

    @Override
    public void deleteRequestById(Long id) {
        MembershipRequest temGroup = reqestsRepository.findById(id).get();
        if(temGroup!=null){
            reqestsRepository.deleteById(id);
        }
    }

    @Override
    public void saveRequest(MembershipRequest membershipRequest) {
        reqestsRepository.save(membershipRequest);
    }
}
