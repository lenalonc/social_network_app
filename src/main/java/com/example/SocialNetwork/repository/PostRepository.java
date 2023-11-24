package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository <Post, Long> {

    //Post findByUser();

}
