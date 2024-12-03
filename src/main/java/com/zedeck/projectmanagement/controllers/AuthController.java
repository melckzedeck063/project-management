package com.zedeck.projectmanagement.controllers;

import com.zedeck.projectmanagement.dtos.LoginDto;
import com.zedeck.projectmanagement.dtos.LoginResponseDto;
import com.zedeck.projectmanagement.dtos.UserAccountDto;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.service.AuthService;
import com.zedeck.projectmanagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        Response<LoginResponseDto> response = authService.login(loginDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody UserAccountDto userAccountDto){
        Response<UserAccount> response = authService.registerUser(userAccountDto);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/me")
    public ResponseEntity<?> getProfile(){
        Response<UserAccount> response = authService.getProfile();
        return ResponseEntity.ok().body(response);
    }

}

