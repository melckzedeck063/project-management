package com.zedeck.projectmanagement.service;


import com.zedeck.projectmanagement.dtos.LoginDto;
import com.zedeck.projectmanagement.dtos.LoginResponseDto;
import com.zedeck.projectmanagement.dtos.UserAccountDto;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.utils.Response;

public interface AuthService {

    Response<LoginResponseDto> login(LoginDto loginDto);

    Response<UserAccount> registerUser(UserAccountDto userAccountDto);

    Response<UserAccount> updateUser(UserAccountDto userAccountDto);

    Response<UserAccount> getProfile();

}

