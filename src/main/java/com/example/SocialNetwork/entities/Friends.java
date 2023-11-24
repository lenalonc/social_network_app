package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "friends")
@Data
public class Friends {

    @EmbeddedId
    private FriendsId id;

    @ManyToOne
    @MapsId("user1Id")
    @JoinColumn(name = "id_user1", nullable = false)
    private User user1Id;

    @ManyToOne
    @MapsId("user2Id")
    @JoinColumn (name = "id_user2", nullable = false)
    private User user2Id;


}
