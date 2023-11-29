package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendRequestService;
import jakarta.persistence.Table;
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
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        friendRequest.setId_user1(user.get().getId());
        return friendRequestService.sendFriendRequest(friendRequest);
    }

    @GetMapping("/")
    public List<FriendRequest> getAllRequests() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long uid = user.get().getId();
        return friendRequestService.getAllRequests(uid);
    }

    @PutMapping("/respond")
    public String respondToRequest(@RequestParam(name = "id") Long id, @RequestParam(name = "status") Long status) {
        return friendRequestService.respondToRequest(id, status);
    }
}
