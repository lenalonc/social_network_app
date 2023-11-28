package com.example.SocialNetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="friends",
            joinColumns = {
                    @JoinColumn(name = "id_user1")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_user2")
            }
    )
    private List<Friends> friends;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "groupmember",
            joinColumns = {
                    @JoinColumn(name = "id_user")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_social_group")
            }
    )

    private List<SocialGroup> socialGroups;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friendrequest",
            joinColumns = {
                    @JoinColumn(name = "id_user1")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_user2")
            }
    )

    private List<FriendRequest> friendRequests;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userfriendrequest", joinColumns = @JoinColumn(name = "user.id"), inverseJoinColumns = @JoinColumn(name = "friendrequest.id"))
    private Set<FriendRequest> friendRequestSet;


    /*@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;*/

}
