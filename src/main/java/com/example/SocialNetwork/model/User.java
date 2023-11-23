package com.example.SocialNetwork.model;

import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name="username", nullable = false, length = 50)
    private String username;

    @Column(name="active",nullable = false)
    private boolean active;



}
