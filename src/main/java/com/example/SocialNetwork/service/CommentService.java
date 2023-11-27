package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsForPost(Long id);

    List<Comment> getAllRepliesForComment(Long id);

    Comment createComment(Comment comment, Long postId);

    Comment replyToComment(Comment reply, Long commentId);

    void deleteCommentById(Long id);


}
