package com.example.SocialNetwork.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "place", nullable = false, length = 50)
    private String place;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_social_group", nullable = false)
    private SocialGroup socialGroup;
    @OneToMany(mappedBy = "event")
    private List<Attending> attendings=new ArrayList<>();

    public void addAttending(Attending attending) {
        attendings.add(attending);
    }
}
