package com.zedeck.projectmanagement.serviceImpl;
import com.zedeck.projectmanagement.dtos.LoginDto;
import com.zedeck.projectmanagement.dtos.LoginResponseDto;
import com.zedeck.projectmanagement.dtos.UserAccountDto;
import com.zedeck.projectmanagement.jwt.JWTUtils;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.service.AuthService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.UserType;
import com.zedeck.projectmanagement.utils.userextractor.LoggedUser;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoggedUser loggedUser;

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private JWTUtils jwtUtils;



    @Override
    public Response<LoginResponseDto> login(LoginDto loginDto) {
        try {
            log.info("LOGIN CREDENTIALS : " , loginDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = UUID.randomUUID().toString();

            Optional<UserAccount> accountOptional = accountRepository.findFirstByUsername(authentication.getName());

            if(accountOptional.isEmpty())
                return new Response<>(true,ResponseCode.FAIL,"Login request failed");

            return getLoginResponseResponse(accountOptional, jwtToken, refreshToken);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Invalid username or password");
    }

    @NotNull
    private Response<LoginResponseDto> getLoginResponseResponse(Optional<UserAccount> accountOptional, String jwtToken, String refreshToken) {
        if (accountOptional.isPresent()){

            UserAccount account = accountOptional.get();
            account.setRefreshToken(refreshToken);
            account.setRefreshTokenCreatedAt(LocalDateTime.now());
            accountRepository.save(account);
            LoginResponseDto response = new LoginResponseDto(
                    jwtToken,
                    refreshToken,
                    "Bearer",
                    account.getUsername(),
                    account.getUserType(),
                    account.getFirstName(),
                    account.getLastName()
            );

            return new Response<>(false, ResponseCode.SUCCESS, response, null, "Login successful");

        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }


    @Override
    public Response<UserAccount> registerUser(UserAccountDto userAccountDto) {
        try {

            if (userAccountDto.getFirstName() == null || userAccountDto.getLastName() == null) {
                return new Response<>(true, ResponseCode.FAIL,"firstname or lastname is null");
            }


            if (userAccountDto.getUsername() == null || userAccountDto.getPassword() == null) {
                return new Response<>(true, ResponseCode.FAIL,"username or password is null");
            }

            if (userAccountDto.getFirstName().isBlank() || userAccountDto.getLastName().isBlank()) {
                return new Response<>(true, ResponseCode.FAIL,"Fill in first name and last name");
            }

            if (userAccountDto.getUsername().isBlank() || userAccountDto.getPassword().isBlank()) {
                return new Response<>(true, ResponseCode.FAIL,"Password must not be empty");
            }

            if (!isValidEmail(userAccountDto.getUsername()))
                return new Response<>(true, ResponseCode.INVALID_REQUEST, null, "Please enter a valid email");

            Optional<UserAccount> firstByUsername = accountRepository.findFirstByUsername(userAccountDto.getUsername());
            if (firstByUsername.isPresent())
                return new Response<>(true, ResponseCode.DUPLICATE_EMAIL, "Duplicate, email already in use");

            Random random = new SecureRandom();
            int nextInt = random.nextInt(100001, 999999);
            UserAccount account = new UserAccount();

            if(userAccountDto.getUserRole() == "" || userAccountDto.getUserRole() == null ){
                account.setUserType(UserType.USER.toString());
            }
            else {
                account.setUserType(userAccountDto.getUserRole());
            }


            account.setEnabled(false);
            account.setUsername(userAccountDto.getUsername());
            account.setFirstName(userAccountDto.getFirstName());
            account.setLastName(userAccountDto.getLastName());
            account.setPassword(passwordEncoder.encode(userAccountDto.getPassword().trim()));
            account.setActive(false);
            account.setUserType(UserType.USER.toString());
            accountRepository.save(account);
            return new Response<>(false, ResponseCode.SUCCESS, account, "Account registered successfully");

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true, ResponseCode.FAIL, null, "Failed to register account unknown error occurred");
    }

    @Override
    public Response<UserAccount> updateUser(UserAccountDto userAccountDto) {
        try {
            UserAccount user =  loggedUser.getUser();

            if(user  == null){
                logger.info("UNAUTHORIZED USER TRYING TO UPDATE PROFILE");
                return new Response<>(true, ResponseCode.UNAUTHORIZED, "Full authentication is required");
            }

            Optional<UserAccount> accountOptional =  accountRepository.findFirstByUsername(userAccountDto.getUsername());
            if(accountOptional.isPresent()){
                UserAccount userAccount1 = accountOptional.get();

                if(userAccountDto.getFirstName() == null){
                    return new Response<>(true,ResponseCode.NULL_ARGUMENT, "Firstname can not be null");
                }

                if(userAccountDto.getLastName() == null){
                    return new Response<>(true, ResponseCode.NULL_ARGUMENT, "Lastname can not be empty");
                }

                if(userAccountDto.getUsername() == null){
                    return new Response<>(true,ResponseCode.NULL_ARGUMENT,"Username can not be empty");
                }

                if(!userAccountDto.getFirstName().isBlank() && !Objects.equals(userAccountDto.getFirstName(),userAccount1.getFirstName()))
                    userAccount1.setFirstName(userAccountDto.getFirstName());

                if(!userAccountDto.getLastName().isBlank() && !Objects.equals(userAccountDto.getLastName(), userAccount1.getLastName()))
                    userAccount1.setLastName(userAccountDto.getLastName());

                if(!userAccountDto.getUsername().isBlank() && !Objects.equals(userAccountDto.getUsername(), userAccount1.getUsername()))
                    if(!isValidEmail(userAccountDto.getUsername())){
                        throw new IllegalAccessException("Invalid email");
                    }
                    else {
                        userAccount1.setUsername(userAccountDto.getUsername());
                    }
                if(userAccountDto.getUserRole() == null){
                    userAccount1.setUserType(String.valueOf(UserType.USER));
                }
                else if (userAccountDto.getUserRole().equalsIgnoreCase(UserType.SUPER_ADMIN.name()))
                    userAccount1.setUserType(String.valueOf(UserType.SUPER_ADMIN));

                else if (userAccountDto.getUserRole().equalsIgnoreCase(UserType.MANAGER.name()))
                    userAccount1.setUserType(String.valueOf(UserType.MANAGER));


                if(userAccount1.getPassword() == null){
                    userAccount1.setPassword(passwordEncoder.encode(userAccountDto.getLastName().toUpperCase().trim()));
                }

                UserAccount userAccount2 =  accountRepository.save(userAccount1);

                return new Response<>(false, ResponseCode.SUCCESS,userAccount2,"User updated successfully");
            }
            else  {
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"User not found");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new Response<>(true,ResponseCode.FAIL,"Failed to create user");
    }

    @Override
    public Response<UserAccount> getProfile() {
        try {
            UserAccount user =  loggedUser.getUser();
            Optional<UserAccount> accountOptional =  accountRepository.findFirstByUsername(user.getUsername());
            return accountOptional.map(userAccount -> new Response<>(false, ResponseCode.SUCCESS, userAccount, "User get successfully")).orElseGet(() -> new Response<>(true, ResponseCode.NO_RECORD_FOUND, "User not found"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Failed to get profile");
    }



    private boolean isValidEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


}
