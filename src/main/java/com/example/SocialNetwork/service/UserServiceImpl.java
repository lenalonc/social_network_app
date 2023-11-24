package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void updateUser(Long id, User user) {
        User tempUser = userRepository.findById(id).get();

        if(tempUser != null){
            if(user.getEmail() != null && !user.getEmail().equals("") && user.getEmail() != tempUser.getEmail()) {
                tempUser.setEmail(user.getEmail());
            }
            if(user.getUsername() != null && !user.getUsername().equals("") && user.getUsername() != tempUser.getUsername()){
                tempUser.setUsername(user.getUsername());
            }
            if(user.isActive() != tempUser.isActive()){
                tempUser.setActive(user.isActive());
            }

        }

        userRepository.save(tempUser);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    public void deleteUserById(Long id) {
        User tempUser = userRepository.findById(id).get();
        if(tempUser!= null) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User findByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

}
