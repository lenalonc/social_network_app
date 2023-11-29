package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    public ResponseEntity<String> updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User tempUser = optionalUser.get();
            if(user.getEmail() != null && !user.getEmail().equals("") && user.getEmail() != tempUser.getEmail()) {
                tempUser.setEmail(user.getEmail());
            }
            if(user.getUsername() != null && !user.getUsername().equals("") && user.getUsername() != tempUser.getUsername()){
                tempUser.setUsername(user.getUsername());
            }
            if(user.isActive() != tempUser.isActive()){
                tempUser.setActive(user.isActive());
            }
            userRepository.save(tempUser);
            return new ResponseEntity<>("User updated", HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    public ResponseEntity<String> deleteUserById(Long id) {
        Optional<User> tempUser = userRepository.findById(id);
        if (tempUser.isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public User findByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        } else
        return null;
    }

}
