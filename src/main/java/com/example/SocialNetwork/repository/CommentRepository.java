package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllById_PostOrderByDate(Long id);
}

