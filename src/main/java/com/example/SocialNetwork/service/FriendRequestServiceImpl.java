package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{

    private FriendRequestRepository friendRequestRepository;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }
    @Override
    public String sendFriendRequest(FriendRequest friendRequest) {
        User user1 = friendRequest.getUser1();
        User user2 = friendRequest.getUser2();

        if(user1.getId() == user2.getId()) {
            return "NE MOZES POSLATI SAM SEBI ZAHTEV AAAAAAA";
        }
        friendRequestRepository.save(friendRequest);
        return "Uspesno poslat zahtev";
    }

    @Override
    public List<FriendRequest> getAllRequests(Long id) {
        return friendRequestRepository.findAllByUser1Id(id);
    }
}
