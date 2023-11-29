package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;

import java.util.List;

public interface FriendsService {
    void saveFriends(Friends friends);

    List<UserDTO> getFriendsByUser(Long userId);
}
