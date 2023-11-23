package com.example.SocialNetwork.model;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "membershiprequest")
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "id_social_group", nullable = false)
    private SocialGroup socialGroup;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
