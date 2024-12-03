package com.zedeck.projectmanagement;



import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.utils.UserType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(Initializer.class);
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            UserAccount userAccount;

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername("admin@ipf.co.tz");

            if (optionalUserAccount.isEmpty()) {
                userAccount = new UserAccount();

                logger.info("=============  CREATING DEFAULT USER ================");

                userAccount.setUsername("admin@ipf.co.tz");
                userAccount.setFirstName("Super");
                userAccount.setLastName("Admin");
                userAccount.setUserType(String.valueOf(UserType.SUPER_ADMIN));
                userAccount.setPassword(passwordEncoder.encode("ipf@2024")); // Call encode() on the injected PasswordEncoder
                userAccount.setEnabled(true);

                userAccountRepository.save(userAccount);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
