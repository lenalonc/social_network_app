package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "friends")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "id_user1", nullable = false)
    private User user1Id;

    @ManyToOne()
    @JoinColumn (name = "id_user2", nullable = false)
    private User user2Id;

}
