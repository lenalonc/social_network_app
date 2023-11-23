package com.example.SocialNetwork.model;

import jakarta.persistence.*;

@Entity
@Table(name = "membershiprequest")
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private RequestStatus requestStatus;

    @OneToMany
    @JoinColumn(name = "id_group")
    private SocialGroup socialGroup;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
