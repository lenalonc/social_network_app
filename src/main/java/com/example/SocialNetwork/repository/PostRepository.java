package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(Long id);

    List<Post> findAllBySocialGroupId(Long id);

    List<Post> findAllByUserIdAndDeleted(Long id, boolean deleted);

    List<Post> findAllBySocialGroupIdAndDeleted(Long id, boolean deleted);

    Post findByIdAndDeleted(Long id, boolean deleted);
}
