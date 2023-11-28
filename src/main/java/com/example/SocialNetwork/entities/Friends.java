package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "friends")
@Data
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user1", nullable = false)
    private User user1Id;

    @ManyToOne
    @JoinColumn (name = "id_user2", nullable = false)
    private User user2Id;


}
