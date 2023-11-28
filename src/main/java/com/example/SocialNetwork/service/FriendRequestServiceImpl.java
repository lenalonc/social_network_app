package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{
    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;
    private FriendsService friendsService;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository, FriendsService friendsService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendsService = friendsService;
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

    @Override
    public String respondToRequest(Long id, Long status) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(id);
        if(friendRequestOptional.isEmpty()){
            return "Friend request not found";
        }
        FriendRequest friendRequest = friendRequestOptional.get();

        Long user1Id = friendRequest.getId_user1();
        Long user2Id = friendRequest.getId_user2();
        Optional<User> user1 = userRepository.findById(user1Id);
        Optional<User> user2 = userRepository.findById(user2Id);

        if(user1.isPresent() && user2.isPresent()){
             User userPrvi = user1.get();
             User userDrugi = user2.get();
             return processRequest(userPrvi, userDrugi, status);
        }
          return "User not found";
    }

    private String processRequest(User userPrvi, User userDrugi, Long status) {
        if (status == 0) {
            Friends newFriend = new Friends();
            newFriend.setUser1Id(userPrvi);
            newFriend.setUser2Id(userDrugi);
            friendsService.saveFriends(newFriend);

            userPrvi.getFriends().add(newFriend);
            userDrugi.getFriends().add(newFriend);
            return "Friend request accepted";
        } else if (status == 1) {
            return "Friend request already on pending list";
        } else if (status == 2) {
            return "Friend request declined";
        } else {
            return "Invalid response";
        }
    }
}
