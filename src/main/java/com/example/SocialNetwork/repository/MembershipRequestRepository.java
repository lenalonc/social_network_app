package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.MembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {
    @Query(value = "SELECT * FROM membershiprequest WHERE id_social_group =:id", nativeQuery = true)
    public List<MembershipRequest> findAllMembershipRequestsForSocialGroup(@Param("id") Long id);
}
