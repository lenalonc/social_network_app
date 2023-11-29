package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{
    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;
    private FriendsRepository friendsRepository;
    private FriendsService friendsService;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository, FriendsService friendsService, FriendsRepository friendsRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendsService = friendsService;
        this.friendsRepository = friendsRepository;
    }
    @Override
    public ResponseEntity<?> sendFriendRequest(FriendRequest friendRequest) {
        Long user1Id = friendRequest.getId_user1();
        Long user2Id = friendRequest.getId_user2();
        if (user1Id.equals(user2Id)) {
            return  new ResponseEntity<>("You can't send a friend request to yourself", HttpStatus.BAD_REQUEST);
        }

        List<User> friends = friendsRepository.getFriendsByUser(user1Id);
        for (User friend : friends) {
            if (friend.getId().equals(user2Id)) {
                return new ResponseEntity<>("You are already friends", HttpStatus.BAD_REQUEST);
            }
        }

        FriendRequest save = friendRequestRepository.save(friendRequest);
        User byId = userRepository.getById(user1Id);
        byId.getFriendRequestSet().add(save);

        return new ResponseEntity<>("Friend request sent", HttpStatus.OK);
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

        List<User> friends = friendsRepository.getFriendsByUser(user1Id);
        for (User friend : friends) {
            if (friend.getId().equals(user2Id)) {
                return "You are already friends";
            }
        }

        if(user1.isPresent() && user2.isPresent()){
             User firstUser = user1.get();
             User secondUser = user2.get();
             return processRequest(firstUser, secondUser, status);
        }
          return "User not found";
    }

    private String processRequest(User firstUser, User secondUser, Long status) {
        if (status == 0) {

            Friends newFriend = new Friends();
            newFriend.setUser1Id(firstUser);
            newFriend.setUser2Id(secondUser);
            friendsService.saveFriends(newFriend);

            firstUser.getFriends().add(newFriend);
            secondUser.getFriends().add(newFriend);
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