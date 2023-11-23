package com.example.SocialNetwork.model;

import jakarta.persistence.*;
@Entity
@Table(name = "friendrequest")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "id_user1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "id_user2", nullable = false)
    private User user2;
}
