package com.example.SocialNetwork.model;


import jakarta.persistence.*;

@Entity
@Table(name="socialgroup")
public class SocialGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name="type", nullable = false)
    private boolean type;

    @ManyToOne
    @JoinColumn(name = "id_admin", nullable = false)
    private User user;

}
