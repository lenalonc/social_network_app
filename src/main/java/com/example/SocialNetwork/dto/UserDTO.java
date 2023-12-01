package com.example.SocialNetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private boolean active;

    private boolean admin;

    private Date doNotDisturb;

    //private List<PostDTO> posts;
}
