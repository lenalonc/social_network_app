package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendsRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsServiceImpl implements FriendsService {

    private FriendsRepository friendsRepository;

    public FriendsServiceImpl(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Override
    @Transactional
    public void saveFriends(Friends friends) {
        friendsRepository.save(friends);
    }

    @Override
    public List<User> getFriendsByUser(Long userId) {
        return friendsRepository.getFriendsByUser(userId);
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