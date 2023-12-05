package com.example.SocialNetwork.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private boolean active;

    private Date doNotDisturb;
}
