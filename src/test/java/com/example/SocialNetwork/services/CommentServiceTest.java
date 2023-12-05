package com.example.SocialNetwork.services;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.CommentRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private FriendsRepository friendsRepository;
    private ModelMapper modelMapper;

    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        this.postRepository = mock(PostRepository.class);
        this.commentRepository = mock(CommentRepository.class);
        this.friendsRepository = mock(FriendsRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.modelMapper = new ModelMapper();
        this.commentService = new CommentServiceImpl(commentRepository, postRepository, userRepository, friendsRepository, modelMapper);
    }

    @Test
    void getAllCommentsForPostSuccessfully() {
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

        Comment comment1 = Comment.builder()
                .id(1L)
                .post(post1)
                .parentComment(null)
                .date(new Date())
                .user(new User())
                .text("com1")
                .build();

        Comment comment2 = Comment.builder()
                .id(2L)
                .post(post1)
                .parentComment(null)
                .date(new Date())
                .user(new User())
                .text("com2")
                .build();

        Friends friends = Friends.builder()
                .id(1L)
                .user1Id(user1)
                .user2Id(user2)
                .build();

        List<SocialGroup> sg = new ArrayList<>();
        sg.add(socialGroup);
        user1.setSocialGroups(sg);

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(postRepository.findById(anyLong())).thenReturn(Optional.ofNullable(post1));

        when(friendsRepository.getFriendIdsByUserId(anyLong())).thenReturn((List.of(friends.getUser1Id().getId(), friends.getUser2Id().getId())));

        when(commentRepository.findAllByPostIdOrderByDate(anyLong())).thenReturn(List.of(comment1, comment2));

        var result = commentService.getAllCommentsForPost(post1.getId());
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllCommentsForPostThrowsNotFoundException() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> commentService.getAllCommentsForPost(1l));

        verify(postRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getAllCommentsForPostThrowsForbiddenException() {
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

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(false)
                .date(new Date())
                .user(user2)
                .socialGroup(null)
                .deleted(false)
                .build();

        SocialGroup socialGroup = SocialGroup.builder()
                .id(1L)
                .name("Group2")
                .user(user1)
                .type(false)
                .build();

        Friends friends = Friends.builder()
                .id(1L)
                .user1Id(new User())
                .user2Id(user2)
                .build();

        user1.setSocialGroups(new ArrayList<>());

        when(postRepository.findById(anyLong())).thenReturn(Optional.ofNullable(post1));

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(friendsRepository.getFriendIdsByUserId(anyLong())).thenReturn((List.of(3L, friends.getUser2Id().getId())));

        assertThrows(ForbiddenException.class, () -> commentService.getAllCommentsForPost(1l));

        verify(postRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(postRepository);
    }


    @Test
    void createCommentSuccessfully() {
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
                .user(user1)
                .type(false)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(false)
                .date(new Date())
                .user(user1)
                .socialGroup(socialGroup)
                .deleted(false)
                .build();

        Comment comment1 = Comment.builder()
                .id(1L)
                .post(post1)
                .parentComment(null)
                .date(new Date())
                .user(new User())
                .text("com1")
                .build();

        Friends friends = Friends.builder()
                .id(1L)
                .user1Id(user1)
                .user2Id(new User())
                .build();

        List<SocialGroup> sg = new ArrayList<>();
        sg.add(socialGroup);
        user1.setSocialGroups(sg);

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));


        when(postRepository.findById(anyLong())).thenReturn(Optional.ofNullable(post1));
        when(friendsRepository.getFriendIdsByUserId(anyLong())).thenReturn((List.of(3L, friends.getUser1Id().getId())));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);

        var result = commentService.createComment(comment1, post1.getId());
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals("com1", result.getText());
    }

    @Test
    void replyToCommentSuccessfully() {
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
                .user(user1)
                .type(false)
                .build();

        Post post1 = Post.builder()
                .id(1L)
                .text("text1")
                .type(false)
                .date(new Date())
                .user(user1)
                .socialGroup(socialGroup)
                .deleted(false)
                .build();

        Comment comment1 = Comment.builder()
                .id(1L)
                .post(post1)
                .parentComment(null)
                .date(new Date())
                .user(new User())
                .text("com1")
                .build();

        Comment comment2 = Comment.builder()
                .id(1L)
                .post(post1)
                .parentComment(null)
                .date(new Date())
                .user(new User())
                .text("com1")
                .build();

        Friends friends = Friends.builder()
                .id(1L)
                .user1Id(user1)
                .user2Id(new User())
                .build();

        List<SocialGroup> sg = new ArrayList<>();
        sg.add(socialGroup);
        user1.setSocialGroups(sg);

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment1));
        when(friendsRepository.getFriendIdsByUserId(anyLong())).thenReturn((List.of(3L, friends.getUser1Id().getId())));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment2);

        var result = commentService.replyToComment(comment2, comment1.getId());
        assertEquals("john@example.com", result.getUser().getEmail());
        assertEquals("com1", result.getText());
    }

    @Test
    void deleteCommentByIdThrowsNotFoundException() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.deleteCommentById(1l));

        verify(commentRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getAllRepliesForCommentSuccessfully() {
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
                .name("Group1")
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

        Comment comment1 = Comment.builder()
                .id(1L)
                .post(post1)
                .parentComment(null)
                .date(new Date())
                .user(new User())
                .text("com1")
                .build();

        Comment reply1 = Comment.builder()
                .id(2L)
                .post(post1)
                .parentComment(comment1)
                .date(new Date())
                .user(user1)
                .text("reply1")
                .build();

        Comment reply2 = Comment.builder()
                .id(3L)
                .post(post1)
                .parentComment(comment1)
                .date(new Date())
                .user(new User())
                .text("reply2")
                .build();

        Friends friends = Friends.builder()
                .id(1L)
                .user1Id(user1)
                .user2Id(new User())
                .build();

        List<SocialGroup> sg = new ArrayList<>();
        sg.add(socialGroup);
        user1.setSocialGroups(sg);

        var authenticationTokenticationToken = new UsernamePasswordAuthenticationToken("test", null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationTokenticationToken);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user1));

        when(commentRepository.findAllByParentId(anyLong())).thenReturn(List.of(reply1, reply2));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment1));
        when(friendsRepository.getFriendIdsByUserId(user2.getId())).thenReturn(List.of(user1.getId(), user2.getId()));

        var result = commentService.getAllRepliesForComment(comment1.getId());
        assertEquals("john@example.com", result.get(0).getUser().getEmail());
        assertEquals("reply1", result.get(0).getText());
    }

}
