package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
