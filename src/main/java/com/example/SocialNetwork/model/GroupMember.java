package com.example.SocialNetwork.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "groupmember")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateJoined", nullable = false)
    private Date dateJoined;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_social_group", nullable = false)
    private SocialGroup socialGroup;

}
