package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsServiceImpl implements FriendsService {
    private ModelMapper mapper;
    private FriendsRepository friendsRepository;

    public FriendsServiceImpl(FriendsRepository friendsRepository,  ModelMapper mapper) {
        this.friendsRepository = friendsRepository;
        this.mapper=mapper;
    }

    @Override
    public void saveFriends(Friends friends) {
        friendsRepository.save(friends);
    }

    @Override
    public List<UserDTO> getFriendsByUser(Long userId) {
        return friendsRepository.getFriendsByUser(userId).stream().map(friend->mapper.map(friend, UserDTO.class)).toList();
    }
}
