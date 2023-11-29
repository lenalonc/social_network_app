package com.example.SocialNetwork.dto;

import com.example.SocialNetwork.entities.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequestDTO {

    private Long id;

    private RequestStatus requestStatus;

    private SocialGroupDTO socialGroup;

    private UserDTO user;

}