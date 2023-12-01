package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.FriendRequestDTO;
import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{
    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;
    private FriendsRepository friendsRepository;
    private FriendsService friendsService;
    private ModelMapper mapper;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository, FriendsService friendsService, FriendsRepository friendsRepository, ModelMapper mapper) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendsService = friendsService;
        this.friendsRepository = friendsRepository;
        this.mapper = mapper;
    }
    @Override
    public ResponseEntity<Object> sendFriendRequest(FriendRequest friendRequest) {
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
    public List<FriendRequestDTO> getAllRequests(Long id) {
        return friendRequestRepository.findAllByUser1Id(id).stream().map(request->mapper.map(request, FriendRequestDTO.class)).toList();
    }

    @Override
    public ResponseEntity<Object> respondToRequest(Long id, Long status) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(id);
        if(friendRequestOptional.isEmpty()){
            return new ResponseEntity<>("Friend request not found", HttpStatus.BAD_REQUEST);
        }
        FriendRequest friendRequest = friendRequestOptional.get();

        Optional<User> user1 = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Long user1Id = user1.get().getId();
        Long user2Id = friendRequest.getId_user2();
        Optional<User> user2 = userRepository.findById(user2Id);

        List<User> friends = friendsRepository.getFriendsByUser(user1Id);
        for (User friend : friends) {
            if (friend.getId().equals(user2Id)) {
                return new ResponseEntity<>("You are already friends", HttpStatus.BAD_REQUEST);
            }
        }

        if(user2.isPresent()){
             User firstUser = user1.get();
             User secondUser = user2.get();
             return processRequest(firstUser, secondUser, status);
        }
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> processRequest(User firstUser, User secondUser, Long status) {
        if (status == 0) {

            Friends newFriend = new Friends();
            newFriend.setUser1Id(firstUser);
            newFriend.setUser2Id(secondUser);
            friendsService.saveFriends(newFriend);

            firstUser.getFriends().add(newFriend);
            secondUser.getFriends().add(newFriend);
            userRepository.save(firstUser);
            userRepository.save(secondUser);

            return new ResponseEntity<>("Friend request accepted", HttpStatus.OK);
        } else if (status == 1) {
            return new ResponseEntity<>("Friend request already on pending list", HttpStatus.OK);
        } else if (status == 2) {
            return new ResponseEntity<>("Friend request declined", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        }
    }

}