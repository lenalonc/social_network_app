
package com.example.SocialNetwork.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

//@Embeddable
@Data
public class FriendsId implements Serializable {

    private Long user1Id;

    private Long user2Id;
}
