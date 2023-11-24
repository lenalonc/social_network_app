package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.SocialGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialGroupRepository extends JpaRepository<SocialGroup, Long> {
   List<SocialGroup> findByName(String name);
}
