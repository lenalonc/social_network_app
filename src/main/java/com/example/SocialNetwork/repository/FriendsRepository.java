package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
}
