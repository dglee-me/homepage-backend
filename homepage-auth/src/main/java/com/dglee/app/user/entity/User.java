package com.dglee.app.user.entity;

import com.dglee.app.user.enums.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String email;

    @NotNull
    String password;

    /**
     * 별명
     */
    @NotBlank
    String username;

    /**
     * 권한
     */
    @ElementCollection
    @Enumerated(EnumType.ORDINAL)
    @Convert(converter = UserRole.class)        
    Collection<? extends GrantedAuthority> authorities;

    @NotNull
    @Builder.Default
    Long createdDate = 0L;

    @NotNull
    @Builder.Default
    Long expiredDate = 0L;

    @NotNull
    @Builder.Default
    Long lastLogonDate = 0L;

    @NotNull
    String status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals("NORMAL");
    }
}