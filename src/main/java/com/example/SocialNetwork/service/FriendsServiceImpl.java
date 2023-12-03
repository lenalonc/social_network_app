package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendsServiceImpl implements FriendsService {
    private ModelMapper mapper;
    private FriendsRepository friendsRepository;
    private UserRepository userRepository;

    public FriendsServiceImpl(FriendsRepository friendsRepository,  ModelMapper mapper, UserRepository userRepository) {
        this.friendsRepository = friendsRepository;
        this.mapper=mapper;
        this.userRepository=userRepository;
    }

    @Override
    @Transactional
    public void saveFriends(Friends friends) {
        friendsRepository.save(friends);
    }

    @Override
    public List<UserDTO> getFriendsByUser() {
        Long userId = getCurrentUser().getId();
        return friendsRepository.getFriendsByUser(userId).stream().filter(user -> user.isActive()).map(friend->mapper.map(friend, UserDTO.class)).toList();
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteFriend(Long friendId) {
        User user = getCurrentUser();

        List<UserDTO> friends = getFriendsByUser();

        for (UserDTO friend : friends) {
            if (friend.getId().equals(friendId)) {
                friendsRepository.deleteFriendByUser(user.getId(), friendId);
                return new ResponseEntity<>("Friend deleted", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Friend not found", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteFriendByUser(Long user1Id, Long user2Id) {
        friendsRepository.deleteFriendByUser(user1Id, user2Id);
        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> searchFriends(String search) {
        User user = getCurrentUser();
        Long userId = user.getId();

        List<UserDTO> searchResults = friendsRepository.searchFriends(userId, search).stream().map(friend->mapper.map(friend, UserDTO.class)).toList();

        if (searchResults.isEmpty()) {
            return new ResponseEntity<>("No friends found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    public User getCurrentUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();
    }
}