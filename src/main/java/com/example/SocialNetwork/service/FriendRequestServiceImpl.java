package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.FriendRequestDTO;
import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.RequestStatus;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.FriendRequestRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public ResponseEntity<Object> sendFriendRequest(FriendRequest friendRequest) {
        Long user1Id = friendRequest.getId_user1();
        Long user2Id = friendRequest.getId_user2();

        User user2 = userRepository.getById(user2Id);
        if(!(user2.isActive())) return new ResponseEntity<>("User is not active", HttpStatus.BAD_REQUEST);

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
        /*User byId = userRepository.getById(user1Id);
        byId.getFriendRequests().add(save);
        userRepository.save(byId);*/

        return new ResponseEntity<>("Friend request sent", HttpStatus.OK);
    }

    @Override
    public List<FriendRequestDTO> getAllRequests(Long id) {
        User user = getCurrentUser();
        return friendRequestRepository.findAllByUser2Id(id).stream().map(request->mapper.map(request, FriendRequestDTO.class)).toList();
    }

    @Override
    @Transactional
    public ResponseEntity<Object> respondToRequest(Long id, Long status) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElseThrow(() -> new NotFoundException("Friend request not found"));

        Optional<User> user1 = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Long user1Id = user1.get().getId();
        Long user2Id = friendRequest.getId_user1();
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
             return processRequest(firstUser, secondUser, status, friendRequest);
        } return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteFriendRequest(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElseThrow(() -> new NotFoundException("Friend request not found"));
        if (friendRequest.getStatus() == RequestStatus.ACCEPTED) {
            return new ResponseEntity<>("Request already accepted, try with delete friend option.", HttpStatus.BAD_REQUEST);
        }
        friendRequestRepository.deletePendingById(id);
        return new ResponseEntity<>("Friend request deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getYourRequests() {
        User user = getCurrentUser();
        List<FriendRequestDTO> sendedFriendRequests = friendRequestRepository.findAllByUser1Id(user.getId()).stream().filter(request -> request.getStatus() == RequestStatus.PENDING).map(request -> mapper.map(request, FriendRequestDTO.class)).toList();

        if (sendedFriendRequests.isEmpty()){
            return new ResponseEntity<>("You have no pending friend requests", HttpStatus.OK);
        }
        return new ResponseEntity<>(sendedFriendRequests, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> processRequest(User firstUser, User secondUser, Long status, FriendRequest friendRequest) {
        if (status == 0) {

            Friends newFriend = new Friends();
            newFriend.setUser1Id(firstUser);
            newFriend.setUser2Id(secondUser);
            friendsService.saveFriends(newFriend);

            friendRequest.setStatus(RequestStatus.ACCEPTED);
            friendRequestRepository.save(friendRequest);

            return new ResponseEntity<>("Friend request accepted", HttpStatus.OK);
        } else if (status == 1) {
            return new ResponseEntity<>("Friend request already on pending list", HttpStatus.OK);
        } else if (status == 2) {
            friendRequest.setStatus(RequestStatus.REJECTED);
            friendRequestRepository.save(friendRequest);
            return new ResponseEntity<>("Friend request declined", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid status", HttpStatus.BAD_REQUEST);
        }
    }

    public User getCurrentUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();
    }
}