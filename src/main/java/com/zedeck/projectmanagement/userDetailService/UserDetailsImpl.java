package com.zedeck.projectmanagement.userDetailService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zedeck.projectmanagement.models.Role;
import com.zedeck.projectmanagement.models.UserAccount;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    @Getter
    private final Long id;
    private final String username;
    @Getter
    private final String firstName;
    @Getter
    private final String lastName;
    @Getter
    private final String uuid;
    @JsonIgnore
    private final String password;
    private final String userType;

    @Enumerated(EnumType.STRING)
    private Role role;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String uuid, String firstName, String lastName, String password, String userType, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = userType;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserAccount user) {
        // Convert user permissions to authorities
        List<GrantedAuthority> authorities = user.getPermissions().stream()
                .map(SimpleGrantedAuthority::new) // Create authorities based on permission strings
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getUserType(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
