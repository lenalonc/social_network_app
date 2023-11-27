package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friendrequest")
@Getter
@Setter
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "id_user1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "id_user2", nullable = false)
    private User user2;
}
