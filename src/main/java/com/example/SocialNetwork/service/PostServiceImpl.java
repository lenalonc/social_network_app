package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.PostDTO;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.FriendsRepository;
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
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private ModelMapper mapper;
    private PostRepository postRepository;
    private SocialGroupRepository socialGroupRepository;

    private UserRepository userRepository;

    private FriendsRepository friendsRepository;

    private EmailService emailService;

    public PostServiceImpl(ModelMapper mapper, PostRepository postRepository, SocialGroupRepository socialGroupRepository,
                           UserRepository userRepository, EmailService emailService, FriendsRepository friendsRepository) {
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
        this.emailService = emailService;
    }

    @Override
    public List<PostDTO> getAllPostsByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist."));
        Optional<User> loggedUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Post> userPosts = postRepository.findAllByUserIdAndDeleted(user.getId(), false);

        List<Post> filteredPosts = userPosts.stream()
                .filter(post -> (post.isType() || (checkFriendship(user, loggedUser.get()))) &&
                        (post.getSocialGroup() == null || checkSocialGroup(post.getSocialGroup(), loggedUser.get())))
                .collect(Collectors.toList());

        return filteredPosts.stream().map(post -> mapper.map(post, PostDTO.class)).toList();
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

        if (!checkSocialGroup(socialGroup, user.get()))
            throw new ForbiddenException("This group is private. You are not a member of this social group." +
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
        post.setSocialGroup(null);

        return this.mapper.map(postRepository.save(post), PostDTO.class);
    }

    @Override
    public PostDTO createPostInGroup(Post post, Long groupId) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SocialGroup socialGroup = socialGroupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Social group does not exist."));

        if (!user.get().getSocialGroups().contains(socialGroup))
            throw new ForbiddenException("User is not a member of this social group." +
                    " Only members post within a group.");

        post.setDate(new Date());
        post.setDeleted(false);
        post.setUser(user.get());
        post.setSocialGroup(socialGroup);
        post.setType(true);

        sendEmails(socialGroup, post);

        return this.mapper.map(postRepository.save(post), PostDTO.class);
    }


    @Override
    public PostDTO updatePost(Long id, Post post) {
        Post tempPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist."));
        if(tempPost.isDeleted()) throw new NotFoundException("Post is deleted.");
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
            throw new ForbiddenException("You do not have permission to delete this post.");
        }
    }

    @Override
    public PostDTO makePostPrivate(Long id) {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post tempPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post does not exist."));

        if (!tempPost.getUser().equals(user.get()))
            throw new ForbiddenException("You cannot alter a post you didn't make.");

        if (tempPost.getSocialGroup() != null)
            throw new ForbiddenException("Posts in social groups must be public!");

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


    private static boolean checkSocialGroup(SocialGroup socialGroup, User loggedUser) {
        if (socialGroup.isType()) return true;
        if (loggedUser.getSocialGroups().contains(socialGroup)) return true;
        return false;
    }

    private boolean checkFriendship(User user, User loggedUser) {
        List<Long> friends = friendsRepository.getFriendIdsByUserId(user.getId());

        if (!(friends.contains(loggedUser.getId()))) {
            return false;
        }
        return true;
    }

    private void sendEmails(SocialGroup socialGroup, Post post) {
        List<User> members = socialGroup.getUsers();
        for (User user : members) {
            String text = "There is a new post in your group: " + socialGroup.getName() +
                    "\n+" + post.getDate() + "\n" + post.getText() + "\nBy: " + post.getUser().getUsername();
            emailService.sendEmail(user.getEmail(), "New post", text);
        }
    }

}
