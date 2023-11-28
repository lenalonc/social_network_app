package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.repository.CommentRepository;
import com.example.SocialNetwork.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    @Override
    public List<Comment> getAllCommentsForPost(Long id) {
        return commentRepository.findAllByPostIdOrderByDate(id);
    }

    @Override
    public List<Comment> getAllRepliesForComment(Long id) {
        //TODO Baciti gresku ako ne postoji komentar sa tim ID-om
        Comment comment = commentRepository.findById(id).get();

        return comment.getReplies();
    }

    @Override
    public Comment createComment(Comment comment, Long postId) {
        //TODO Baciti gresku ako objava ne postoji
        Post post = postRepository.findById(postId).get();
        comment.setPost(post);
        comment.setDate(LocalDateTime.now());
        //TODO Loggedin user da se stavi kao user

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
