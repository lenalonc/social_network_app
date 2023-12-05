package com.example.SocialNetwork.dtos;

import com.example.SocialNetwork.entities.RequestStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequestDTO {

    private Long id;

    private RequestStatus requestStatus;

    private SocialGroupDTO socialGroup;

    private UserDTO user;

}