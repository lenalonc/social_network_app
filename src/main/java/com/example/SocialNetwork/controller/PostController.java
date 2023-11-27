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

//ova metoda trreba da se odradi kad budem imali login
    @GetMapping("/all/{id}")
    public List<Post> getAllPostsByUser(@PathVariable Long id){
        return postService.getAllPostsByUser(id);
    }
    @GetMapping("/all")
    public List<Post> getAllPostsByLoggedInUser(){
        return postService.getAllPostsByLoggedInUser();
    }
    @GetMapping("/all/group/{id}")
    public List<Post> getAllPostsBySocialGroup(@PathVariable Long id){
        return postService.getAllPostsBySocialGroup(id);
    }

    @PostMapping("/")
    public String createPost(@RequestBody Post post) {
        postService.createPost(post);
        return "Bravo";
    }

    @PostMapping("/group_post")
    public String createPostInGroup(@RequestBody Post post){
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
