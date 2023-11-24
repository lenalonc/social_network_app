package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.service.FriendsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friendrequest")
public class FriendsController {

    private FriendsService friendsService;

    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @PostMapping("/")
    public void sendFriendRequest(@RequestBody Friends friends) {
        friendsService.saveFriends(friends);
    }
}
