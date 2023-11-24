package com.example.SocialNetwork.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "id_admin", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "socialGroup")
//    private ArrayList<MembershipRequest> membershipRequest;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "socialGroups")
    private List<User> users;
}
