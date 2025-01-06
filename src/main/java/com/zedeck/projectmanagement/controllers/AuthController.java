package com.zedeck.projectmanagement.controllers;

import com.zedeck.projectmanagement.dtos.LoginDto;
import com.zedeck.projectmanagement.dtos.LoginResponseDto;
import com.zedeck.projectmanagement.dtos.UserAccountDto;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.service.AuthService;
import com.zedeck.projectmanagement.service.LogoutService;
import com.zedeck.projectmanagement.utils.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LogoutService logoutService;

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

    @PutMapping("/update-user")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> updateUser(@RequestBody UserAccountDto userAccountDto){
        Response<UserAccount> response = authService.updateUser(userAccountDto);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/me")
    public ResponseEntity<?> getProfile(){
        Response<UserAccount> response = authService.getProfile();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return "Logged out successfully!";
    }

}

