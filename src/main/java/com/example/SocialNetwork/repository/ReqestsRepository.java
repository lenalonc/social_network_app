package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.MembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReqestsRepository extends JpaRepository<MembershipRequest, Long> {
}
