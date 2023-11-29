package com.example.SocialNetwork.dto;

import com.example.SocialNetwork.entities.Comment;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;

    private LocalDateTime date;



    private String text;

    private boolean type;

    private boolean deleted;

    private UserDTO user;

}
