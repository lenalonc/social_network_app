package com.example.SocialNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

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
    private RequestStatus status = RequestStatus.PENDING;

    private Long id_user1;

    private Long id_user2;

    @Column(name = "date", nullable = false)
    private Date date;

    public RequestStatus isAccepted() {
        return status;
    }
}
