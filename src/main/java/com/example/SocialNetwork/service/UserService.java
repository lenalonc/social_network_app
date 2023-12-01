package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    UserDTO updateUser(Long id, UserUpdateDto userUpdateDto);

    List<UserDTO> getAllUsers();

    ResponseEntity<String> deleteUserById(Long id);

    UserDTO findByID(Long id);

    User findCurrentUser();

    UserDTO findByUsername(String name);
}
