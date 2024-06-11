package com.giansiccardi.springcloud.auth_eventos.config;


import com.giansiccardi.springcloud.auth_eventos.entidades.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetails implements UserDetails {

   private String email;
   private String password;
    private List<String> roles;


    public CustomUserDetails(Usuario usuario) {
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.roles = usuario.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
