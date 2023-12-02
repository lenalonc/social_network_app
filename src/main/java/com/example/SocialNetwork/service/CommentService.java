package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.CommentDTO;
import com.example.SocialNetwork.entities.Comment;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getAllCommentsForPost(Long id);

    List<CommentDTO> getAllRepliesForComment(Long id);

    CommentDTO createComment(Comment comment, Long postId);

    CommentDTO replyToComment(Comment reply, Long commentId);

    void deleteCommentById(Long id);


}
