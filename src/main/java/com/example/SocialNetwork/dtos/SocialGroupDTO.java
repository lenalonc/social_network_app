package com.example.SocialNetwork.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialGroupDTO {

    private Long id;

    private String name;

    private boolean type;

    private UserDTO user;
}
