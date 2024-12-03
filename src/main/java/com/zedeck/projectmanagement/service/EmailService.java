package com.zedeck.projectmanagement.service;

import com.zedeck.projectmanagement.dtos.EmailDto;
import com.zedeck.projectmanagement.utils.Response;
import org.springframework.stereotype.Service;


public interface EmailService {
    Response<EmailDto> sendEmail(EmailDto emailDto);
}
