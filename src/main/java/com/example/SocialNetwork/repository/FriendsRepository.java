package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    @Query(
            "SELECT user2Id FROM Friends WHERE user1Id.id = :user1id"
    )
    List<User> getFriendsByUser(@Param("user1id") Long userId);
}
