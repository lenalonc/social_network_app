package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.FriendsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {

    private FriendsService friendsService;

    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @PostMapping("/")
    public void sendFriendRequest(@RequestBody Friends friends) {
        Friends friends1=new Friends();
        friends1.setUser1Id(friends.getUser2Id());
        friends1.setUser2Id(friends.getUser1Id());
        friendsService.saveFriends(friends);
        friendsService.saveFriends(friends1);
    }

    @GetMapping("/")
    public List<UserDTO> getFriendsByUser(@RequestParam("user1Id") Long userId) {
        return friendsService.getFriendsByUser(userId);
    }
}
