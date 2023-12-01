package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.PostDTO;
import com.example.SocialNetwork.entities.Post;

import java.util.List;


public interface PostService {


    List<PostDTO> getAllPostsByUser(Long id);

    List<PostDTO> getAllPostsByLoggedInUser();

    List<PostDTO> getAllPostsBySocialGroup(Long id);

    PostDTO createPost(Post post);

    PostDTO createPostInGroup(Post post, Long groupId);

    PostDTO updatePost(Long id, Post post);

    void deletePostById(Long id);

    PostDTO makePostPrivate(Long id);

    PostDTO makePostPublic(Long id);
}
