package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.FriendRequest;

import java.util.List;

public interface FriendRequestService {
    String sendFriendRequest(FriendRequest friendRequest);

    List<FriendRequest> getAllRequests(Long id);
}
