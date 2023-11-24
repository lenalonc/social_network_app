package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor

public class PostController {

private PostService postService;




//ova metida treba da se dovrsi
    @GetMapping("/all/{id}")
    public List<Post> getAllPostsByUser(@PathVariable Long id){
        return postService.getAllPostsByUser(id);
    }

    @PostMapping("/")
    public String createPost(@RequestBody Post post) {
        postService.createPost(post);
        return "Bravo";
    }

    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id,@RequestBody Post post) {
        postService.updatePost(id, post);
        return "Bravo";
    }


    @DeleteMapping("/{id}")
    public String deletePostById(@PathVariable Long id){
        postService.deletePostById(id);
        return "Bravo";
    }

    @PutMapping("/makePrivate/{id}")
    public String makePostPrivate(@PathVariable Long id){
        postService.makePostPrivate(id);
        return "Bravo";
    }
    @PutMapping("/makePublic/{id}")
    public String makePostPublic(@PathVariable Long id){
        postService.makePostPublic(id);
        return "Bravo";
    }




}
