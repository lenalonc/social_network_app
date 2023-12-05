package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.dtos.UserUpdateDto;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface UserService {
    void saveUser(User user);

    UserDTO updateUser(Long id, UserUpdateDto userUpdateDto);

    List<UserDTO> getAllUsers();

    ResponseEntity<String> deleteUserById(Long id);

    UserDTO findByID(Long id);

    User findCurrentUser();

    UserDTO findByUsername(String name);

    ResponseEntity<Object> setDoNotDisturb(int days);
}
