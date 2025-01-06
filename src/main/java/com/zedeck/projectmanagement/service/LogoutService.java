package com.zedeck.projectmanagement.service;

import com.zedeck.projectmanagement.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogoutService implements LogoutHandler {

   @Autowired
   private TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        System.out.println("Logout method called");
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header is missing or does not start with 'Bearer '");
            return;
        }

        final String jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if (storedToken == null) {
            return;
        }

        storedToken.setRevoked(true);
        storedToken.setExpired(true);
//        System.out.println("Token revoked and expired: " + jwt);
        tokenRepository.save(storedToken);

        SecurityContextHolder.clearContext();
        System.out.println("Logged out successfully");
    }

}
