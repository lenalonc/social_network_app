package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "friends")
@Data
public class Friends {
/*
    @EmbeddedId
    private FriendsId id;*/

    @Id
    private Long id3;

    @ManyToOne
    @JoinColumn(name = "user1Id", nullable = false)
    private User user1Id;

    @ManyToOne
    @JoinColumn (name = "user2Id", nullable = false)
    private User user2Id;


}
