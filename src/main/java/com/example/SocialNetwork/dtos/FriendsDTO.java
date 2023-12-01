package com.example.SocialNetwork.dtos;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendsDTO {

    private Long id;

    private UserDTO user1Id;

    private UserDTO user2Id;

}
