package com.example.SocialNetwork.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    @NotBlank
    private String password;
    @NotBlank
    private String secretKey;

}
