package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Attending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendingRepository extends JpaRepository<Attending, Long> {
    public List<Attending> findAllByEventId(Long id);
}
