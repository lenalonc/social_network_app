package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.FriendRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

   @Query(value = "SELECT * FROM friendrequest e WHERE e.id_user1 = :uid", nativeQuery = true)
    List<FriendRequest> findAllByUser1Id(@Param("uid") Long uid);

}
