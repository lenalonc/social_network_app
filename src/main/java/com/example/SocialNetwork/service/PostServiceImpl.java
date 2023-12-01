package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.PostDTO;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private ModelMapper mapper;
    private PostRepository postRepository;
    private SocialGroupRepository socialGroupRepository;

    private UserRepository userRepository;

    private EmailService emailService;

    public PostServiceImpl(ModelMapper mapper, PostRepository postRepository, SocialGroupRepository socialGroupRepository,
                           UserRepository userRepository, EmailService emailService) {
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public List<PostDTO> getAllPostsByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist."));
        List<Post> userPosts = postRepository.findAllByUserIdAndDeleted(user.getId(), false);
        userPosts = filterPosts(userPosts, user);
        return userPosts.stream().map(userPost -> mapper.map(userPost, PostDTO.class)).toList();
    }


    @Override
    public List<PostDTO> getAllPostsByLoggedInUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Post> userPosts = postRepository.findAllByUserIdAndDeleted(user.get().getId(), false);
        return userPosts.stream().map(userPost -> mapper.map(userPost, PostDTO.class)).toList();
    }


    @Override
    public List<PostDTO> getAllPostsBySocialGroup(Long id) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SocialGroup socialGroup = socialGroupRepository.findById(id).orElseThrow(() -> new NotFoundException("Social group not found."));
        if (!user.get().getSocialGroups().contains(socialGroup))
            throw new ForbiddenException("User is not a member of this social group." +
                    " Only members can see posts within a group.");
        List<Post> postsInGroup = postRepository.findAllBySocialGroupIdAndDeleted(id, false);
        return postsInGroup.stream().map(post -> mapper.map(post, PostDTO.class)).toList();
    }

    @Override
    public PostDTO createPost(Post post) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        post.setUser(user.get());
        post.setDate(new Date());
        post.setDeleted(false);

        PostDTO postDTO = this.mapper.map(postRepository.save(post), PostDTO.class);

        return postDTO;
    }

    @Override
    public PostDTO createPostInGroup(Post post, Long groupId) {

        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Social group does not exist."));
        if (!user.get().getSocialGroups().contains(socialGroup))
            throw new ForbiddenException("User is not a member of this social group." +
                    " Only members can see posts within a group.");

        post.setDate(new Date());
        post.setDeleted(false);
        post.setUser(user.get());
        post.setSocialGroup(socialGroup);
        post.setType(true);

        PostDTO postDTO = this.mapper.map(postRepository.save(post), PostDTO.class);

        sendEmails(socialGroup);

        return this.mapper.map(postRepository.save(post), PostDTO.class);

    }


    @Override
    public PostDTO updatePost(Long id, Post post) {
        System.out.println(id);
        Post tempPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist."));
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!tempPost.getUser().equals(user.get()))
            throw new ForbiddenException("You can't alter a post you didn't make.");
        if (post.getText() == null || post.getText().equals("")) throw new ForbiddenException("Posts cannot be empty.");

        tempPost.setText(post.getText());
        return this.mapper.map(postRepository.save(tempPost), PostDTO.class);
    }

    @Override
    public void deletePostById(Long id) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist."));

        if (user.get().equals(post.getUser()) ||
                (post.getSocialGroup() != null && post.getSocialGroup().getUser().equals(user.get()))) {
            postRepository.deleteById(id);
        } else {
            throw new ForbiddenException("User does not have permission to delete this post.");
        }

    }

    @Override
    public PostDTO makePostPrivate(Long id) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post tempPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist."));

        if (!tempPost.getUser().equals(user.get()))
            throw new ForbiddenException("You can't alter a post you didn't make.");

        tempPost.setType(false);
        return this.mapper.map(postRepository.save(tempPost), PostDTO.class);
    }

    @Override
    public PostDTO makePostPublic(Long id) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post tempPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist."));

        if (!tempPost.getUser().equals(user.get()))
            throw new ForbiddenException("You can't alter a post you didn't make.");

        tempPost.setType(true);
        return this.mapper.map(postRepository.save(tempPost), PostDTO.class);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void getUnexpiredPosts() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (is24HoursOrMoreAfter(post.getDate())) {
                post.setDeleted(true);
            }
        }
        postRepository.saveAll(posts);
    }

    private static boolean is24HoursOrMoreAfter(Date dateToCheck) {
        long timeDifference = System.currentTimeMillis() - dateToCheck.getTime();
        long hoursDifference = timeDifference / (60 * 60 * 1000);
        return hoursDifference >= 24;
    }

    private List<Post> filterPosts(List<Post> userPosts, User user) {
        Optional<User> loggeduser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Post> posts = new ArrayList<>();
        for (Post post : userPosts) {
            if (!post.isType()) {
                if (!user.getFriends().contains(loggeduser))
                    continue;
            }
            posts.add(post);
        }
        return posts;
    }

    private void sendEmails(SocialGroup socialGroup) {
        List<User> members = socialGroup.getUsers();
        for (User user : members) {
            String text = "There is a new post in your group: " + socialGroup.getName();
            emailService.sendEmail(user.getEmail(), "New post", text);
        }
    }

}
