package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendsService {
    void saveFriends(Friends friends);

    List<UserDTO> getFriendsByUser();

    ResponseEntity<String> deleteFriend(Long friendId);

    ResponseEntity<String> deleteFriendByUser(Long user1Id, Long user2Id);

    ResponseEntity<?> searchFriends(String search);
}
