package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.FriendRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

   @Query(value = "SELECT * FROM friendrequest e WHERE e.id_user1 = :uid", nativeQuery = true)
    List<FriendRequest> findAllByUser1Id(@Param("uid") Long uid);

   @Query(value = "SELECT * FROM friendrequest e WHERE e.id_user2 = :uid", nativeQuery = true)
    List<FriendRequest> findAllByUser2Id(@Param("uid") Long id);

   @Modifying
   @Query(value = "DELETE FROM friendrequest e WHERE e.id = :id", nativeQuery = true)
    void deletePendingById(Long id);
}
