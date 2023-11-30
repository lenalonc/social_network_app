package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.CommentDTO;
import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.CommentRepository;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public User getCurrentUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();
    }

    @Override
    public List<CommentDTO> getAllCommentsForPost(Long id) {
        User user = getCurrentUser();

        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        if (post.getSocialGroup() != null) {
            for(SocialGroup s: user.getSocialGroups()){
                if(s.getId().equals(post.getSocialGroup().getId())){
                    return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
                }
            } throw new NotFoundException("You are not in this group");
        }

        if (!( post.isType())){
            List<Friends> friends = user.getFriends();
            for (Friends friend : friends) {
                if (friend.getUser2Id().equals(post.getUser().getId())) {
                    return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
                }
            }
        } else throw new ForbiddenException("You are not allowed to see this comments");

        return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }

    @Override
    public List<CommentDTO> getAllRepliesForComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if(comment.isPresent()) {
            return comment.get().getReplies().stream().map(reply -> modelMapper.map(reply, CommentDTO.class)).toList();
        }
        throw new NotFoundException("Comment with id " + id + " not found");

    }

    @Override
    public Comment createComment(Comment comment, Long postId) {
        User user = getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post with id " + postId + " not found"));
        boolean privateChecksFlag = false;

        if (!(post.isType())){
            List<Friends> friends = user.getFriends();
            for (Friends friend : friends) {
                if (friend.getUser2Id().equals(post.getUser().getId())) {
                    privateChecksFlag = true;
                }
            }
        } if (!privateChecksFlag) throw new ForbiddenException("You are not allowed to comment on this post, because you are not friends");

        comment.setPost(post);
        comment.setDate(LocalDateTime.now());
        comment.setUser(user);
        boolean groupChecksFlag = false;

        if (post.getSocialGroup() != null) {

            for(SocialGroup s: user.getSocialGroups()){
                if(s.getId().equals(post.getSocialGroup().getId())){
                   groupChecksFlag = true;
                }
            }
            if (!groupChecksFlag) throw new NotFoundException("You are not in this group");
        }

        List<Comment> comments = post.getComments();
        comments.add(comment);
        user.getComments().add(comment);
        postRepository.save(post);
        userRepository.save(user);
        return commentRepository.save(comment);

    }

    @Override
    public Comment replyToComment(Comment reply, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getParentComment() != null) {
            throw new ForbiddenException("You can't reply to a reply");
        }

        User user = getCurrentUser();

        if (comment.getPost().getSocialGroup() != null) {
            boolean groupChecksFlag = false;
            for(SocialGroup s: user.getSocialGroups()){
                if(s.getId().equals(comment.getPost().getSocialGroup().getId())){
                    groupChecksFlag = true;
                }
            } if (!groupChecksFlag) throw new NotFoundException("You are not in this group");
        }

        if (!(comment.getPost().isType())){
            List<Friends> friends = user.getFriends();
            boolean privateChecksFlag = false;
            for (Friends friend : friends) {
                if (friend.getUser2Id().equals(comment.getPost().getUser().getId())) {
                    privateChecksFlag = true;
                }
            } if (!privateChecksFlag) throw new ForbiddenException("You are not allowed to comment on this post, because you are not friends");
        }

        reply.setUser(user);
        reply.setDate(LocalDateTime.now());
        reply.setParentComment(comment);

        comment.getReplies().add(reply);
        commentRepository.save(comment);

        user.getComments().add(reply);
        userRepository.save(user);

        return commentRepository.save(reply);
    }

    @Override
    public void deleteCommentById(Long id) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));
        User user = getCurrentUser();

        if (comment.getUser().getId().equals(user.getId()) || comment.getPost().getSocialGroup().getUser().getId().equals(user.getId()) || comment.getParentComment().getUser().getId().equals(user.getId())) {
            user.getComments().remove(comment);
            commentRepository.deleteById(id);
            userRepository.save(user);
        } else {
            throw new NotFoundException("You are not allowed to delete this comment");
        }

    }
}
