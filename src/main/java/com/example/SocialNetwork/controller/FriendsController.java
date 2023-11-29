package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    private FriendsService friendsService;
    private UserRepository userRepository;

    public FriendsController(FriendsService friendsService, UserRepository userRepository) {
        this.friendsService = friendsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public ResponseEntity<String> sendFriendRequest(@RequestBody Friends friends) {
        friendsService.saveFriends(friends);

        return new ResponseEntity<>("Friend request sent", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getFriendsByUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long userId = user.get().getId();
        List<User> friends = friendsService.getFriendsByUser(userId);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteFriend(@RequestParam("friendId") Long friendId) {
        return friendsService.deleteFriend(friendId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFriendByUser(@RequestParam("user1Id") Long user1Id, @RequestParam("user2Id") Long user2Id) {
        return friendsService.deleteFriendByUser(user1Id, user2Id);
    }
}
