package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    public List<FriendRequest> findAllByUser1Id(long id);
}
