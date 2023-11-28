package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.service.FriendRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendrequests")
public class FriendRequestController {

    private FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/")
    public String sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        FriendRequest friendRequest1 = new FriendRequest();
        return friendRequestService.sendFriendRequest(friendRequest);
    }

    @GetMapping("/")
    public List<FriendRequest> getAllRequests(@RequestParam(name = "uid") Long uid) {
        return friendRequestService.getAllRequests(uid);
    }
}
