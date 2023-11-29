package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendsRepository;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Transactional
    public void saveFriends(Friends friends) {
        friendsRepository.save(friends);
    }

    @Override
    public List<UserDTO> getFriendsByUser(Long userId) {
        return friendsRepository.getFriendsByUser(userId).stream().map(friend->mapper.map(friend, UserDTO.class)).toList();
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteFriend(Long friendId) {
        friendsRepository.deleteById(friendId);
        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteFriendByUser(Long user1Id, Long user2Id) {
        friendsRepository.deleteFriendByUser(user1Id, user2Id);
        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);

    }
}