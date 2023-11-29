package com.example.SocialNetwork.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    private List<String> roles;

}
