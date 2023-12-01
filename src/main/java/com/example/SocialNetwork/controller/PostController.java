package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor

public class PostController {

    private PostService postService;

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

    @PutMapping("/makePrivate/{id}")
    public ResponseEntity<?> makePostPrivate(@PathVariable Long id) {
        return ResponseEntity.ok(postService.makePostPrivate(id));
    }

    @PutMapping("/makePublic/{id}")
    public ResponseEntity<?> makePostPublic(@PathVariable Long id) {
        return ResponseEntity.ok(postService.makePostPublic(id));
    }

}
