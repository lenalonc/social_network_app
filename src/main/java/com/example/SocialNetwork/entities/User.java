package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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

    @Column(name = "donotdisturb")
    private Date donotdistrub;


    @ManyToMany()
    @JoinTable(name="friends",
            joinColumns = {
                    @JoinColumn(name = "id_user1")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_user2")
            }
    )
    private List<Friends> friends;

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

    @ManyToMany()
    @JoinTable(name = "user_friendrequest",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "friendrequest_id")
            }
    )
    private List<FriendRequest> friendRequests;

    @ManyToMany()
    @JoinTable(name = "user_friendrequest", joinColumns = @JoinColumn(name = "user.id"), inverseJoinColumns = @JoinColumn(name = "friendrequest.id"))
    private Set<FriendRequest> friendRequestSet;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

}
