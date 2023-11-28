package com.example.SocialNetwork.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "type", nullable = false)
    private boolean type;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_social_group", nullable = false)
    private SocialGroup socialGroup;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;


}
