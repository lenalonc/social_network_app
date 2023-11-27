package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllById_User(Long id);
    List<Post> findAllById_Social_Group(Long id);

}
