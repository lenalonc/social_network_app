package com.example.SocialNetwork.dto;

import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.Post;
import com.example.SocialNetwork.entities.SocialGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private String password;

    private boolean active;

    private boolean admin;

    private Date donotdistrub;

    //private List<PostDTO> posts;
}
