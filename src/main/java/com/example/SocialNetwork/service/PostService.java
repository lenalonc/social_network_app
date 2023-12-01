package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.PostDTO;
import com.example.SocialNetwork.entities.Post;

import java.util.List;


public interface PostService {


    List<Post> getAllPostsByUser(Long id);

    List<Post> getAllPostsByLoggedInUser();

    List<PostDTO> getAllPostsBySocialGroup(Long id);

    Post createPost(Post post);

    Post createPostInGroup(Post post, Long groupId);

    Post updatePost(Long id, Post post);

    void deletePostById(Long id);

    Post makePostPrivate(Long id);

    Post makePostPublic(Long id);
}
