package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Post;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PostService {


    Post createPost(Post post);

    Post updatePost(Long id, Post post);


    List<Post> getAllPostsByUser(Long id);

    List<Post> getAllPosts();

    void deletePostById(Long id);

    Post makePostPrivate(Long id);

    Post makePostPublic(Long id);
}
