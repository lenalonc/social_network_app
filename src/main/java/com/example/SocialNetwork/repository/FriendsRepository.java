package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    @Query(
            "SELECT u FROM User u WHERE u.id IN ( SELECT CASE WHEN f.user1Id.id = :userId THEN f.user2Id.id ELSE f.user1Id.id END FROM Friends f WHERE f.user1Id.id = :userId OR f.user2Id.id = :userId)"
    )
    List<User> getFriendsByUser(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN f.user1Id.id = :id THEN f.user2Id.id ELSE f.user1Id.id END FROM Friends f WHERE f.user1Id.id = :id OR f.user2Id.id = :id")
    List<Long> getFriendIdsByUserId(@Param("id") Long userId);

    @Modifying
    @Query(
            "DELETE FROM Friends WHERE user1Id.id = :user1id AND user2Id.id= :user2id"
    )
    void deleteFriendByUser(@Param("user1id") Long user1Id, @Param("user2id") Long user2Id);


    @Query("SELECT u FROM User u JOIN Friends f ON (u.id = f.user1Id.id OR u.id = f.user2Id.id) WHERE (f.user1Id.id = :id OR f.user2Id.id = :id) AND u.username LIKE %:search%")
    List<User> searchFriends(@Param("id") Long userId, @Param("search") String search);
}
