package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.FriendRequestDTO;
import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friendrequests")
public class FriendRequestController {

    private FriendRequestService friendRequestService;

    private UserRepository userRepository;

    public FriendRequestController(FriendRequestService friendRequestService, UserRepository userRepository) {
        this.friendRequestService = friendRequestService;
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public ResponseEntity<Object> sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        friendRequest.setId_user1(user.get().getId());
        return friendRequestService.sendFriendRequest(friendRequest);
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllRequests() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long uid = user.get().getId();
        List<FriendRequestDTO> listOfFriends = friendRequestService.getAllRequests(uid);

        if (listOfFriends.isEmpty()) {
            return ResponseEntity.ok("You have no friend requests");
        } else {
            return ResponseEntity.ok(listOfFriends);
        }
    }

    @PutMapping("/respond")
    public ResponseEntity<Object> respondToRequest(@RequestParam(name = "id") Long id, @RequestParam(name = "status") Long status) {
        return friendRequestService.respondToRequest(id, status);
    }
}
