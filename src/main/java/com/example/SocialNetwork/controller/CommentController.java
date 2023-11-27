package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.service.CommentService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("replies/{id}")
    public ResponseEntity<?> getAllRepliesForComment(@PathVariable Long idComment) {
        return ResponseEntity.ok(commentService.getAllRepliesForComment(idComment));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createComment(@RequestBody Comment comment, @PathVariable Long postId) {
        return ResponseEntity.ok(commentService.createComment(comment, postId));
    }

    @PostMapping("/reply/{id}")
    public ResponseEntity<?> replyToComment(@RequestBody Comment reply, @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.replyToComment(reply, commentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok().build();
    }


}
