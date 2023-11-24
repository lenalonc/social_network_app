package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    PostRepository postRepository;

    @Override
    public Post createPost(Post post) {
        //post.setDate(LocalDateTime.now());

        post.setDeleted(false);
        //TODO: ulogovani user se stavlja kao user id
        //TODO: metod za kreiranje posta u grupi

      return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long id, Post post) {
        Post tempPost = postRepository.getById(id); //TODO Provreriti da nije null i baciti gresku ako jeste

        //TODO Da se proveri da li je ulogovani korisnik isti kao ovaj sto apdejtuje post

        if(tempPost != null){
            if(post.getText() != null && !post.getText().equals("") && post.getText() != tempPost.getText()) {
                tempPost.setText(post.getText());
            }
        }
        return postRepository.save(tempPost);
    }

    @Override
    public List<Post> getAllPostsByUser(Long id) {
        return null;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post makePostPrivate(Long id) {
        Post tempPost = postRepository.findById(id).get();
        tempPost.setType(true);
        return postRepository.save(tempPost);
    }

    @Override
    public Post makePostPublic(Long id) {
        Post tempPost = postRepository.findById(id).get();
        tempPost.setType(false);
        return postRepository.save(tempPost);
    }
}
