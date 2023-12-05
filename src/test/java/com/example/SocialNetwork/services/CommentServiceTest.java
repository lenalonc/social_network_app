package com.example.SocialNetwork.services;

import com.example.SocialNetwork.repository.CommentRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.mock;

public class CommentServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private FriendsRepository friendsRepository;
    private ModelMapper modelMapper;
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        this.commentRepository = mock(CommentRepository.class);
        this.postRepository = mock(PostRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.friendsRepository = mock(FriendsRepository.class);
        this.modelMapper = new ModelMapper();
        this.commentService = new CommentServiceImpl(commentRepository, postRepository, userRepository, friendsRepository, modelMapper);
    }


}
