package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.PostDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.EmailService;
import com.example.SocialNetwork.service.PostService;
import com.example.SocialNetwork.service.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    private UserRepository userRepository;
    private EmailService emailService;

    private SocialGroupRepository socialGroupRepository;

    private FriendsRepository friendsRepository;
    private PostRepository postRepository;
    private PostServiceImpl postService;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        this.postRepository = mock(PostRepository.class);
        this.postService = mock(PostServiceImpl.class);
        this.emailService = mock(EmailService.class);
        this.friendsRepository = mock(FriendsRepository.class);
        this.socialGroupRepository = mock(SocialGroupRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.modelMapper = new ModelMapper();
        this.postService = new PostServiceImpl(modelMapper, postRepository, socialGroupRepository, userRepository, emailService, friendsRepository);
    }

    @Test
    void getAllPostsByLoggedInUserSuccessfully() {
        User user1 = User.builder()
                .id(235L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(219L)
                .name("Group2")
                .user(user1)
                .type(true)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(user1)
                .socialGroup(socialGroup)
                .deleted(false)
                .build();

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findAllByUserIdAndDeleted(user1.getId(), false)).thenReturn(List.of(post1));

        var result = postService.getAllPostsByLoggedInUser();

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    void getAllPostsByUserSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("alice@example.com")
                .username("Alice")
                .active(true)
                .doNotDisturb(null)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("Group2")
                .user(user1)
                .type(false)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(false)
                .date(new Date())
                .user(user2)
                .socialGroup(socialGroup)
                .deleted(false)
                .build();

        Friends friends = Friends.builder()
                .id(1L)
                .user1Id(user1)
                .user2Id(user2)
                .build();

        List<SocialGroup> socialGroups = new ArrayList<>();
        socialGroups.add(socialGroup);
        user1.setSocialGroups(socialGroups);


        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        when(postRepository.findAllByUserIdAndDeleted(user2.getId(), false)).thenReturn(List.of(post1));

        when(friendsRepository.getFriendIdsByUserId(user2.getId())).thenReturn(List.of(friends.getUser1Id().getId(), friends.getUser2Id().getId()));

        var result = postService.getAllPostsByUser(user2.getId());
        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    void getAllPostByUserThrowsNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> postService.getAllPostsByUser(1l));

        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getAllPostsBySocialGroupSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("alice@example.com")
                .username("Alice")
                .active(true)
                .doNotDisturb(null)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("Group2")
                .user(user1)
                .type(false)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(user2)
                .socialGroup(socialGroup)
                .deleted(false)
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .text("text2")
                .type(true)
                .date(new Date())
                .user(user2)
                .socialGroup(socialGroup)
                .deleted(false)
                .build();

        List<SocialGroup> socialGroups = new ArrayList<>();
        socialGroups.add(socialGroup);
        user1.setSocialGroups(socialGroups);

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(socialGroupRepository.findById(any())).thenReturn(Optional.ofNullable(socialGroup));
        when(postRepository.findAllBySocialGroupIdAndDeleted(socialGroup.getId(), false)).thenReturn(List.of(post1, post2));

        var result = postService.getAllPostsBySocialGroup(socialGroup.getId());
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllPostsBySocialGroupThrowsNotFoundException() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(socialGroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> postService.getAllPostsBySocialGroup(1l));

        verify(socialGroupRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(socialGroupRepository);
    }

    @Test
    void getAllPostsBySocialGroupThrowsForbiddenException() {

        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("Group2")
                .user(new User())
                .type(false)
                .build();

        user1.setSocialGroups(new ArrayList<>());

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(socialGroupRepository.findById(any())).thenReturn(Optional.ofNullable(socialGroup));

        assertThrows(ForbiddenException.class, () -> postService.getAllPostsBySocialGroup(1l));
    }

    @Test
    void createPostSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();


        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(user1)
                .socialGroup(null)
                .deleted(false)
                .build();

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.save(any(Post.class))).thenReturn(post1);

        var result = postService.createPost(post1);
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals("text1", result.getText());

        verify(postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void createPostInGroupSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("alice@example.com")
                .username("Alice")
                .active(true)
                .doNotDisturb(null)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("Group2")
                .user(user1)
                .type(false)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(false)
                .date(null)
                .user(null)
                .socialGroup(null)
                .deleted(false)
                .build();

        List<SocialGroup> socialGroups = new ArrayList<>();
        socialGroups.add(socialGroup);
        user1.setSocialGroups(socialGroups);

        List<User> members = new ArrayList<>();
        members.add(user1);
        members.add(user2);
        socialGroup.setUsers(members);

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(socialGroupRepository.findById(any())).thenReturn(Optional.ofNullable(socialGroup));
        when(postRepository.save(any(Post.class))).thenReturn(post1);

        var result = postService.createPostInGroup(post1, socialGroup.getId());
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals("text1", result.getText());
    }

    @Test
    void updatePostSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(user1)
                .socialGroup(null)
                .deleted(false)
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .text("text2")
                .type(true)
                .date(new Date())
                .user(user1)
                .socialGroup(null)
                .deleted(false)
                .build();


        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));
        post1.setText(post2.getText());
        when(postRepository.save(any(Post.class))).thenReturn(post1);


        var result = postService.updatePost(post1.getId(), post2);
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals("text2", result.getText());
    }

    @Test
    void updatePostThrowsForbiddenException() {
        User user1 = User.builder()
                .id(1L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(new User())
                .socialGroup(null)
                .deleted(false)
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(new User())
                .socialGroup(null)
                .deleted(false)
                .build();

        user1.setSocialGroups(new ArrayList<>());

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post1));

        assertThrows(ForbiddenException.class, () -> postService.updatePost(2L, post1));
    }

    @Test
    void deletePostByIdThrowsNotFoundException() {
        User user1 = User.builder()
                .id(2L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> postService.deletePostById(1l));

        verify(postRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(postRepository);
    }


    @Test
    void makePostPrivateSuccessfully() {
        User user1 = User.builder()
                .id(2L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(true)
                .date(new Date())
                .user(user1)
                .socialGroup(null)
                .deleted(false)
                .build();

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findById(1l)).thenReturn(Optional.ofNullable(post1));

        post1.setType(false);

        when(postRepository.save(any(Post.class))).thenReturn(post1);

        var result = postService.makePostPrivate(post1.getId());
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals(false, result.isType());
    }

    @Test
    void makePostPublicSuccessfully() {
        User user1 = User.builder()
                .id(2L)
                .email("john@example.com")
                .username("John")
                .active(true)
                .doNotDisturb(null)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(false)
                .date(new Date())
                .user(user1)
                .socialGroup(null)
                .deleted(false)
                .build();

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findById(1l)).thenReturn(Optional.ofNullable(post1));

        post1.setType(true);

        when(postRepository.save(any(Post.class))).thenReturn(post1);

        var result = postService.makePostPublic(post1.getId());
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals(true, result.isType());
    }


}
