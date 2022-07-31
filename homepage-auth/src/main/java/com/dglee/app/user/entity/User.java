package com.dglee.app.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    List<String> roles = new ArrayList<>();

    @NotNull
    @Column (name = "createdDate")
    @Builder.Default
    Long createdDate = 0L;

    @NotNull
    @Column (name = "expiredDate")
    @Builder.Default
    Long expiredDate = 0L;

    @NotNull
    @Column (name = "lastLogonDate")
    @Builder.Default
    Long lastLogonDate = 0L;

    @NotNull
    @Column(name = "status")
    String status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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