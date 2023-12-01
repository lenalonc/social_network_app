package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    private FriendsService friendsService;

    public FriendsController(FriendsService friendsService, UserRepository userRepository) {
        this.friendsService = friendsService;
    }

    @PostMapping("/")
    public ResponseEntity<String> sendFriendRequest(@RequestBody Friends friends) {
        Friends friends1=new Friends();
        friends1.setUser1Id(friends.getUser2Id());
        friends1.setUser2Id(friends.getUser1Id());
        friendsService.saveFriends(friends);
        friendsService.saveFriends(friends1);

        return new ResponseEntity<>("Friend request sent", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getFriendsByUser() {
        List<UserDTO> friends = friendsService.getFriendsByUser();
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
