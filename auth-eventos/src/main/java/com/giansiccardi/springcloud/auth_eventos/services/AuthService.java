package com.giansiccardi.springcloud.auth_eventos.services;

import com.giansiccardi.springcloud.auth_eventos.entidades.Usuario;
import com.giansiccardi.springcloud.auth_eventos.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private WebClient client;

    @Autowired
    UsuarioRepository usuarioRepository;

    private Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;


    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
@Autowired
    private JWTservices jwTservices;


    public String generateToken(String userName ,List<String> roles){
        return jwTservices.generateToken(userName,roles);
    }

    public void validarToken(String token){
        jwTservices.validateToken(token);
    }


    @Transactional
    public Usuario crearUsuario(Usuario usuario){
        log.info("Creando usuario: {}", usuario);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        usuario.setRoles(roles);
        log.info("Roles asignados al usuario: {}", usuario.getRoles());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }




}
