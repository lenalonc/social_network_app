package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.CommentDTO;
import com.example.SocialNetwork.entities.Comment;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getAllCommentsForPost(Long id);

    List<CommentDTO> getAllRepliesForComment(Long id);

    Comment createComment(Comment comment, Long postId);

    Comment replyToComment(Comment reply, Long commentId);

    void deleteCommentById(Long id);


}
