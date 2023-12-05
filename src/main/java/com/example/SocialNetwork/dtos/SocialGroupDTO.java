package com.example.SocialNetwork.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SocialGroupDTO {

    private Long id;

    private String name;

    private boolean type;

    private UserDTO user;
}
