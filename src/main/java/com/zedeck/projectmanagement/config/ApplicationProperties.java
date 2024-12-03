package com.zedeck.projectmanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.zedeck.projectmanagement")
public class ApplicationProperties {

    private String jwtKey;

    private String defaultPassword;

    private int maxFailedLoginCount =3;

    private int cacheMaxLimit = 100;

    private String apiKey;

    private String secretKey;

    private String sourceAddress;

    private String downstreamUrl;

}
