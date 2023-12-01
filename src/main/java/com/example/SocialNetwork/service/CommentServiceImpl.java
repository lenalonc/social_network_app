package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.CommentDTO;
import com.example.SocialNetwork.dto.UserDTO;
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

import java.time.LocalDateTime;
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

    public User getCurrentUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();
    }

    @Override
    public List<CommentDTO> getAllCommentsForPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));
        if(post.isDeleted()) throw new NotFoundException("Post with id " + id + " is deleted");
        User user = getCurrentUser();

        isInSocialGroup(post, user);

        if (!(post.isType())) {

            /*List<Friends> friends = user.getFriends();
            boolean privateChecksFlag = false;
            for (Friends friend : friends) {
                if (friend.getUser2Id().equals(post.getUser().getId())) {
                    privateChecksFlag = true;
                }
            } if (!privateChecksFlag) throw new ForbiddenException("You are not allowed to comment on this post, because you are not friends");*/

        }

        return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();



        /*User user = getCurrentUser();

        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));

        boolean userinSocialGroup;

        if (post.getSocialGroup() != null) {
            for(SocialGroup s: user.getSocialGroups()){
                if(s.getId().equals(post.getSocialGroup().getId())){
                    userinSocialGroup = true;
                }
            } if (!(userinSocialGroup)) new NotFoundException("You are not in this group");
        }

        if (!( post.isType())){

            List<User> friends = friendsRepository.getFriendsByUser(user.getId());

            for (User u: friends){
                if (u.getId().equals(post.getUser().getId())){
                    return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
                }
            } throw new ForbiddenException("You are not allowed to see this comments");

        return commentRepository.findAllByPostIdOrderByDate(id).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();*/
        }

    private static void isInSocialGroup(Post post, User user) {
        if (post.getSocialGroup() != null) {
            boolean groupChecksFlag = false;
            for(SocialGroup s: user.getSocialGroups()){
                if(s.getId().equals(post.getSocialGroup().getId())){
                    groupChecksFlag = true;
                }
            } if (!groupChecksFlag) throw new ForbiddenException("You are not in this group");
        }
    }


    @Override
    public List<CommentDTO> getAllRepliesForComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));
        return comment.getReplies().stream().map(reply -> modelMapper.map(reply, CommentDTO.class)).toList();
    }

    @Override
    public CommentDTO createComment(Comment comment, Long postId) {

        User user = getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post with id " + postId + " not found"));

        if(post.isDeleted()) throw new NotFoundException("Post with id " + postId + " is deleted");

        boolean privateChecksFlag = false;

        if (!(post.isType())){
            /*
        } if (!privateChecksFlag) throw new ForbiddenException("You are not allowed to comment on this post, because you are not friends");*/}

        comment.setPost(post);
        comment.setDate(new Date());
        comment.setUser(user);
        isInSocialGroup(post, user);

        CommentDTO commentDTO = modelMapper.map(commentRepository.save(comment), CommentDTO.class);
        return this.modelMapper.map(commentRepository.save(comment), CommentDTO.class);
    }

    @Override
    public Comment replyToComment(Comment reply, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getParentComment() != null) {
            throw new ForbiddenException("You can't reply to a reply");
        }

        User user = getCurrentUser();

        isInSocialGroup(comment.getPost(), user);

        if (!(comment.getPost().isType())){

        }

        reply.setUser(user);
        reply.setDate(new Date());
        reply.setParentComment(comment);

        comment.getReplies().add(reply);
        commentRepository.save(comment);

       /*user.getComments().add(reply);
        userRepository.save(user);*/

        return commentRepository.save(reply);
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
}
