package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameIgnoreCase(String name);
}
