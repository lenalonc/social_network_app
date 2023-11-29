package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.FriendsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    private FriendsService friendsService;

    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @PostMapping("/")
    public void sendFriendRequest(@RequestBody Friends friends) {
        friendsService.saveFriends(friends);
    }

    @GetMapping("/")
    public List<User> getFriendsByUser(@RequestParam("user1Id") Long userId) {
        return friendsService.getFriendsByUser(userId);
    }

    @DeleteMapping("/")
    public String deleteFriend(@RequestParam("friendId") Long friendId) {
        return friendsService.deleteFriend(friendId);
    }

    @DeleteMapping("/delete")
    public String deleteFriendByUser(@RequestParam("user1Id") Long user1Id, @RequestParam("user2Id") Long user2Id) {
        return friendsService.deleteFriendByUser(user1Id, user2Id);
    }
}
