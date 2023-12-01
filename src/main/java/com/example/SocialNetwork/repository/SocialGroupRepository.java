package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.SocialGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialGroupRepository extends JpaRepository<SocialGroup, Long> {
   List<SocialGroup> findByName(String name);

    @Modifying
    @Query(value = "DELETE FROM socialgroup WHERE id=:id_group AND id_admin =:id_user",nativeQuery = true)
    void deleteByIdAndUserId(@Param("id_group") Long id_group, @Param("id_user") Long id_user);
}
