package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.service.CommentService;
import com.example.SocialNetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private PostService postService;

    private CommentService commentService;

    @GetMapping("/")
    public ResponseEntity<?> getAllPostsByLoggedInUser() {
        return ResponseEntity.ok(postService.getAllPostsByLoggedInUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllPostsByUser(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getAllPostsByUser(id));
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<?> getAllPostsBySocialGroup(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getAllPostsBySocialGroup(id));
    }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @PostMapping("/group/{id}")
    public ResponseEntity<?> createPostInGroup(@RequestBody Post post, @PathVariable Long id) {
        return ResponseEntity.ok(postService.createPostInGroup(post, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@RequestBody Post post, @PathVariable Long id) {
        return ResponseEntity.ok(postService.updatePost(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/make_private/{id}")
    public ResponseEntity<?> makePostPrivate(@PathVariable Long id) {
        return ResponseEntity.ok(postService.makePostPrivate(id));
    }

    @PutMapping("/make_public/{id}")
    public ResponseEntity<?> makePostPublic(@PathVariable Long id) {
        return ResponseEntity.ok(postService.makePostPublic(id));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> getAllCommentsForPost(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getAllCommentsForPost(id));
    }

    @GetMapping("comments/replies/{id}")
    public ResponseEntity<?> getAllRepliesForComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getAllRepliesForComment(id));
    }

    @PostMapping("comments/{id}")
    public ResponseEntity<?> createComment(@RequestBody Comment comment, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.createComment(comment, id));
    }

    @PostMapping("comments/reply/{id}")
    public ResponseEntity<?> replyToComment(@RequestBody Comment reply, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.replyToComment(reply, id));
    }

    @DeleteMapping("comments/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok().build();
    }

}
