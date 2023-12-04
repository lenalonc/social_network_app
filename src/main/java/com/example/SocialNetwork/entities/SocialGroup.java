package com.example.SocialNetwork.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="socialgroup")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "socialGroup")
    @JsonIgnore
    private List<MembershipRequest> membershipRequest;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "socialGroups", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "socialGroup")
    private List<Post> posts;

    @Override
    public String toString() {
        return "";
    }
}
