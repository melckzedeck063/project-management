package com.zedeck.projectmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String refreshToken;
    private String tokenType;
    private String username;
    private String userType;
    private String firstName;
    private String lastName;

}
