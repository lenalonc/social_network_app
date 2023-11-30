package com.example.SocialNetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "membershiprequest")
@Getter
@Setter
@NoArgsConstructor
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "status", nullable = false)
    private RequestStatus requestStatus;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_social_group", nullable = false)
    private SocialGroup socialGroup;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
