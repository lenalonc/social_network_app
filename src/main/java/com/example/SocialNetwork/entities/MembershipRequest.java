package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "membershiprequest")
@Data
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
