package com.giansiccardi.springcloud.auth_eventos.config;


import com.giansiccardi.springcloud.auth_eventos.entidades.Usuario;
import com.giansiccardi.springcloud.auth_eventos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsServices implements UserDetailsService {
    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<Usuario>u= repository.findByEmail(username);
        return u.map(CustomUserDetails::new).orElseThrow(()->new UsernameNotFoundException("USUARIO NO ENCOTRADO"));
    }
}
