package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    @Query(
            "SELECT user2Id FROM Friends WHERE user1Id.id = :user1id"
    )
    List<User> getFriendsByUser(@Param("user1id") Long userId);

    @Modifying
    @Query(
            "DELETE FROM Friends WHERE user1Id.id = :user1id AND user2Id.id= :user2id"
    )
    void deleteFriendByUser(@Param("user1id") Long user1Id, @Param("user2id") Long user2Id);
}
