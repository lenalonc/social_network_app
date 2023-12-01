package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    ResponseEntity<String> updateUser(Long id, User user);

    List<UserDTO> getAllUsers();

    ResponseEntity<String> deleteUserById(Long id);

    User findByID(Long id);

    UserDTO findByIDDTO(Long id);

    public User findCurrentUser();

    UserDTO findByUsername(String name);
}
