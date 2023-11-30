package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    @Query(value = "SELECT id_user FROM groupmember WHERE id_social_group =:id", nativeQuery = true)
    public List<Long> findAllM(@Param("id") Long id);

    @Query(value = "DELETE FROM groupmember WHERE id_user =:id",nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);
}
