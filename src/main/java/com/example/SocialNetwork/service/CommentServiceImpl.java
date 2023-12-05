package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.CommentDTO;
import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.CommentRepository;
import com.example.SocialNetwork.repository.FriendsRepository;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private FriendsRepository friendsRepository;
    private ModelMapper modelMapper;

    @Override
    public List<CommentDTO> getAllCommentsForPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));
        if (post.isDeleted()) throw new NotFoundException("Post with id " + id + " is deleted");
        User user = getCurrentUser();

        checkSocialGroup(post, user);

        checkFriendship(post, user);

        return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }


    @Override
    public List<CommentDTO> getAllRepliesForComment(Long id) {
        List<Comment> replies = commentRepository.findAllByParentId(id);
        if (replies.isEmpty()) throw new NotFoundException("Comment with id " + id + " has no replies");

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));

        checkSocialGroup(comment.getPost(), getCurrentUser());
        checkFriendship(comment.getPost(), getCurrentUser());

        return replies.stream().map(reply -> modelMapper.map(reply, CommentDTO.class)).toList();
    }

    @Override
    public CommentDTO createComment(Comment comment, Long postId) {

        User user = getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post with id " + postId + " not found"));

        if (post.isDeleted()) throw new NotFoundException("Post with id " + postId + " is deleted");

        if(!(post.getUser().equals(user))){
            checkFriendship(post, user);
        }

        comment.setPost(post);
        comment.setDate(new Date());
        comment.setUser(user);

        checkSocialGroup(post, user);

        CommentDTO commentDTO = modelMapper.map(commentRepository.save(comment), CommentDTO.class);
        return commentDTO;
    }


    @Override
    public CommentDTO replyToComment(Comment reply, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getParentComment() != null) {
            throw new ForbiddenException("You can't reply to a reply");
        }

        User user = getCurrentUser();

        if(!(comment.getUser().equals(user))){
            checkFriendship(comment.getPost(), user);
        }

        checkSocialGroup(comment.getPost(), user);

        reply.setUser(user);
        reply.setDate(new Date());
        reply.setParentComment(comment);
        reply.setPost(comment.getPost());
        commentRepository.save(reply);

        CommentDTO commentDTO = modelMapper.map(reply, CommentDTO.class);

        return commentDTO;
    }

    @Override
    public void deleteCommentById(Long id) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));
        User user = getCurrentUser();

        if (comment.getUser().getId().equals(user.getId()) || comment.getPost().getSocialGroup().getUser().getId().equals(user.getId()) || comment.getParentComment().getUser().getId().equals(user.getId())) {
            commentRepository.deleteByCommentId(id);
        } else {
            throw new NotFoundException("You are not allowed to delete this comment");
        }

    }

    private static void checkSocialGroup(Post post, User user) {
        if (post.getSocialGroup() != null) {
            boolean groupChecksFlag = false;
            for (SocialGroup s : user.getSocialGroups()) {
                if (s.getId().equals(post.getSocialGroup().getId())) {
                    groupChecksFlag = true;
                }
            }
            if (!groupChecksFlag) throw new ForbiddenException("You are not in this group");
        }
    }

    private void checkFriendship(Post post, User user) {
        if (!(post.isType())) {
            List<Long> friends = friendsRepository.getFriendIdsByUserId(post.getUser().getId());

            if (!(friends.contains(user.getId()))) {
                throw new ForbiddenException("You are not allowed to see this post");
            }
        }
    }

    public User getCurrentUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();
    }
}
