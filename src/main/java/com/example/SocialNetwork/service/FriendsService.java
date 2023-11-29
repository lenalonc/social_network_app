package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;

import java.util.List;

public interface FriendsService {
    void saveFriends(Friends friends);

    List<User> getFriendsByUser(Long userId);

    String deleteFriend(Long friendId);

    String deleteFriendByUser(Long user1Id, Long user2Id);
}
