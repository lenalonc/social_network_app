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

    @ManyToOne
    @JoinColumn(name = "id_social_group")
    private SocialGroup  socialGroup;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;




}


//package com.example.SocialNetwork.model;
//
//        import jakarta.persistence.*;
//        import java.util.Date;
//
//@Entity
//@Table(name = "event")
//public class Event {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "date", nullable = false)
//    private Date date;
//
//    @Column(name = "place", nullable = false, length = 50)
//    private String place;
//
//    @ManyToOne
//    @JoinColumn(name = "id_user", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "id_social_group", nullable = false)
//    private SocialGroup socialGroup;
//
//
//}