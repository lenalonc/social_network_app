package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllBySocialGroupId(Long id);
}
