package com.example.SocialNetwork.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;

@Entity
@Table(name="socialgroup")
@Data
public class SocialGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name="type", nullable = false)
    private boolean type;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "socialGroup")
    private ArrayList<MembershipRequest> membershipRequest;
}
