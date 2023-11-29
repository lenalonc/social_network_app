package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.CommentRepository;
import com.example.SocialNetwork.repository.PostRepository;
import com.example.SocialNetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
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

    @Override
    public List<Comment> getAllCommentsForPost(Long id) {
        return commentRepository.findAllByPostIdOrderByDate(id);
    }

    @Override
    public List<Comment> getAllRepliesForComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if(comment.isPresent()) {
            return comment.get().getReplies();
        }
        throw new NotFoundException("Comment with id " + id + " not found");

    }

    @Override
    public Comment createComment(Comment comment, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post with id " + postId + " not found"));
        comment.setPost(post);
        comment.setDate(LocalDateTime.now());

        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        comment.setUser(user.get());


        if (post.getSocialGroup() != null) {
            //TODO Uraditi proveru da li je ulogovani korisnik u toj grupi, tj. sme da komentira na toj objavi
            // Baciti gresku ako nije


        }

        List<Comment> comments = post.getComments();
        comments.add(comment);
        postRepository.save(post);

        return commentRepository.save(comment);

    }

    @Override
    public Comment replyToComment(Comment reply, Long commentId) {
        //TODO Ako ne postoji komentar sa ID-om baciti gresku
        Comment comment = commentRepository.findById(commentId).get();

        if (comment.getParentComment() != null) {
            //TODO Baciti gresku jer bi ovo bio u suprotnom reply na reply
        }

        reply.setDate(LocalDateTime.now());
        reply.setParentComment(comment);

        comment.getReplies().add(reply);
        commentRepository.save(comment);

        return commentRepository.save(reply);
    }

    @Override
    public void deleteCommentById(Long id) {
        // Smes brisati kad si ti napisao taj komentar, kada je komentar u grupi i ti si admin grupe, kada je komentar odgovor na tvoj kometnar

        //TODO Opet greska ako ne postoji komentar
        Comment comment = commentRepository.findById(id).get();

        //TODO Provere za ulogovanog u vezi svega gornjeg

    }
}
