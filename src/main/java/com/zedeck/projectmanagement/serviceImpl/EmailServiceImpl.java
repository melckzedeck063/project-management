package com.zedeck.projectmanagement.serviceImpl;

import com.zedeck.projectmanagement.dtos.EmailDto;
import com.zedeck.projectmanagement.service.EmailService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Response<EmailDto> sendEmail(EmailDto emailDto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        System.out.println(emailDto.getBody().toUpperCase());
        System.out.println(emailDto.getRecipient().toUpperCase());

        try {
            helper.setFrom("drpptcleaners@gmail.com");
            helper.setTo(emailDto.getRecipient());
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getBody(), true);
            javaMailSender.send(message);

            System.out.println("Email sent successfully to: " + emailDto.getRecipient());
            return new Response<>(true, ResponseCode.SUCCESS, "Email sent successfully");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(true, ResponseCode.FAIL, "Failed to send email. Check logs for details.");
        }

    }
}
