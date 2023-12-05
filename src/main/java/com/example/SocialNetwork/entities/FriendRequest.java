package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "friendrequest")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    private Long id_user1;

    private Long id_user2;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_friendrequest",
            joinColumns = {
                    @JoinColumn(name = "id_friendrequest")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "id_user")
            })
    private List<User> users;

    @Column(name = "date", nullable = false)
    private Date date;

    public RequestStatus isAccepted() {
        return status;
    }
}
