package com.example.SocialNetwork.dto;
import com.example.SocialNetwork.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendsDTO {

    private Long id;

    private UserDTO user1Id;

    private UserDTO user2Id;

}
