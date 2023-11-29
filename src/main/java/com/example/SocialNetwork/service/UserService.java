package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    void updateUser(Long id, User user);

    List<UserDTO> getAllUsers();

    void deleteUserById(Long id);

    User findByID(Long id);

    UserDTO findByIDDTO(Long id);

    public User findCurrentUser();
}
