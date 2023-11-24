package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "admin", nullable = false)
    private boolean admin;
/*
    @ManyToMany
    @JoinTable(name="friends",
            joinColumns = {
                    @JoinColumn(name = "id_user1")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_user2")
            }
    )
    private List<Friends> friends;
*/
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "groupmember",
            joinColumns = {
                    @JoinColumn(name = "id_user")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_social_group")
            }
    )
    private List<SocialGroup> socialGroups;

    @ManyToMany
    @JoinTable(name = "friendrequest",
            joinColumns = {
                    @JoinColumn(name = "id_user1")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_user2")
            }
    )
    private List<FriendRequest> friendRequests;

}
