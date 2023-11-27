package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository <Post, Long> {

    Post findAllById_User(User user);

}
