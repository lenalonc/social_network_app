package com.example.SocialNetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = false)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @JsonIgnore
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

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @JsonIgnore
    private String secretKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> roles;

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.singleton(new SimpleGrantedAuthority("UNCONFIRMED"));
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

}
