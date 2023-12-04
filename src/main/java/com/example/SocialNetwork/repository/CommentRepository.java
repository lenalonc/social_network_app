package com.example.SocialNetwork.repository;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByDate(Long id);

    @Modifying
    @Query(value = "DELETE FROM comment WHERE id=:id_comment",nativeQuery = true)
    void deleteByCommentId(@Param("id_comment") Long id_comment);

    @Query(value = "SELECT * FROM comment WHERE id_parent_com=:id",nativeQuery = true)
    List<Comment> findAllByParentId(Long id);

}

