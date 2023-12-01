package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attending")
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Attending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_event", nullable=false)
    private Event event;
}
