package com.zedeck.projectmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {

    private String uuid;
    private String username;
    private String firstName;
    private String lastName;
    private String userRole;
    private String password;


}

