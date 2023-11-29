package com.example.SocialNetwork.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @OneToMany
    @JoinColumn(name = "id")
    private List<Comment> replies;

    @ManyToOne
    @JoinColumn(name = "id_parent_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;


}
