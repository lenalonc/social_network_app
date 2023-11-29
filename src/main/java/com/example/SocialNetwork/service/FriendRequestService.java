package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.FriendRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendRequestService {
    ResponseEntity<?> sendFriendRequest(FriendRequest friendRequest);

    List<FriendRequest> getAllRequests(Long id);

    String respondToRequest(Long id, Long status);
}
