package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{
    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }
    @Override
    public String sendFriendRequest(FriendRequest friendRequest) {
        Long user1Id = friendRequest.getId_user1();
        Long user2Id = friendRequest.getId_user2();
        if (user1Id.equals(user2Id)) {
            return "You can't send a friend request to yourself";
        }
        FriendRequest save = friendRequestRepository.save(friendRequest);
        User byId = userRepository.getById(user1Id);
        byId.getFriendRequestSet().add(save);
        return "Friend request sent";
    }

    @Override
    public List<FriendRequest> getAllRequests(Long id) {
        return friendRequestRepository.findAllByUser1Id(id);
    }
}
