package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.PostDTO;
import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private ModelMapper mapper;
    private PostRepository postRepository;
    private SocialGroupRepository socialGroupRepository;

    private UserRepository userRepository;

    public PostServiceImpl(ModelMapper mapper, PostRepository postRepository, SocialGroupRepository socialGroupRepository,
                           UserRepository userRepository) {
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Post> getAllPostsByUser(Long id) {
        List<Post> userPosts = postRepository.findAllByUserId(id);

        return getUnexpiredPosts(userPosts);
    }

    @Override
    public List<PostDTO> getAllPostsByLoggedInUser() {
        //TODO Kada bude uradjen login, onda samo treba preko ulogovanog uzeti sve njegove objave
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        ModelMapper mapper = new ModelMapper();
        //List<Post> userPosts = postRepository.findAllByUserId(user.get().getId());

        //List<User> users = userRepository.findAll();
        //return users.stream().map(user->mapper.map(user, UserDTO.class)).toList();

        List<Post> userPosts = getUnexpiredPosts(postRepository.findAllByUserId(user.get().getId()));
        return userPosts.stream().map(userPost->mapper.map(userPost, PostDTO.class)).toList();
    }

    @Override
    public List<PostDTO> getAllPostsBySocialGroup(Long id) {
        //TODO Uraditi proveru da je ulogovani korisnik u grupi u kojoj se traze objave
        List<Post> postsInGroup = postRepository.findAllBySocialGroupId(id);
        postsInGroup = getUnexpiredPosts(postsInGroup);

        return postsInGroup.stream().map(post -> mapper.map(post, PostDTO.class)).toList();
    }

    @Override
    public Post createPost(Post post) {
        //Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<User> user = userRepository.findById(6l);
        post.setUser(user.get());
        post.setDate((LocalDateTime.now()));
        post.setDeleted(false);

        return postRepository.save(post);
    }

    @Override
    public Post createPostInGroup(Post post, Long groupId) {
        //TODO Kada bude sredjen email servis, onda treba poslati notifikacije svim clanovima grupe
        //TODO Uzeti ulogovanog usera i proveriti da li je on clan grupe u kojoj vrsi objavu

        //post.setDate(LocalDateTime.now());
        post.setDeleted(false);
        //TODO: ulogovani user se stavlja kao user id

        //TODO Baciti gresku ako nema grupe sa ovim ID-om
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).get();

        List<Post> socialGroupPosts = socialGroup.getPosts();
        socialGroupPosts.add(post);
        socialGroupRepository.save(socialGroup);
        post.setSocialGroup(socialGroup);

        return postRepository.save(post);

    }

    @Override
    public Post updatePost(Long id, Post post) {
        Post tempPost = postRepository.getById(id); //TODO Provreriti da nije null i baciti gresku ako jeste

        //TODO Da se proveri da li je ulogovani korisnik isti kao ovaj sto apdejtuje post

        if (tempPost != null) {
            if (post.getText() != null && !post.getText().equals("") && post.getText() != tempPost.getText()) {
                tempPost.setText(post.getText());
            }
        }
        return postRepository.save(tempPost);
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


    private List<Post> getUnexpiredPosts(List<Post> unfilteredPosts) {
        List<Post> unexpiredPosts = new ArrayList<>();
        for (Post post : unfilteredPosts) {
            //if (!post.getDate().isAfter(post.getDate().plusHours(24))) unexpiredPosts.add(post);
        }
        return unexpiredPosts;
    }
}
