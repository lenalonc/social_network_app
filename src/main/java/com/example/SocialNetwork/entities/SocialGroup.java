package com.example.SocialNetwork.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="socialgroup")
@Getter
@Setter
@NoArgsConstructor
public class SocialGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name="type", nullable = false)
    private boolean type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "socialGroup")
//    private ArrayList<MembershipRequest> membershipRequest;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "socialGroups", fetch = FetchType.LAZY)
    private List<User> users;
}
